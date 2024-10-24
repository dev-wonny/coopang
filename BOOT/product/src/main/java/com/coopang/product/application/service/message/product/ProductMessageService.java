package com.coopang.product.application.service.message.product;

import com.coopang.apicommunication.feignclient.noti.NotiClientService;
import com.coopang.apicommunication.kafka.consumer.MessageService;
import com.coopang.apicommunication.kafka.message.CompleteProduct;
import com.coopang.apicommunication.kafka.message.ErrorProduct;
import com.coopang.apicommunication.kafka.message.ProcessProduct;
import com.coopang.apicommunication.kafka.message.RollbackProduct;
import com.coopang.apicommunication.kafka.producer.MessageProducer;
import com.coopang.apiconfig.datetime.DateTimeUtil;
import com.coopang.apidata.application.company.response.CompanyResponse;
import com.coopang.apidata.application.noti.enums.SlackMessageStatus;
import com.coopang.apidata.application.noti.request.CreateSlackMessageRequest;
import com.coopang.apidata.application.user.response.UserResponse;
import com.coopang.product.application.request.productstock.ProductStockDto;
import com.coopang.product.application.response.product.ProductResponseDto;
import com.coopang.product.application.service.feignclient.CompanyFeignClientService;
import com.coopang.product.application.service.feignclient.UserFeignClientService;
import com.coopang.product.application.service.product.ProductService;
import com.coopang.product.application.service.productstock.ProductStockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j(topic = "ProductMessageService")
@Service("productMessageService")
@RequiredArgsConstructor
public class ProductMessageService implements MessageService {

    private final ObjectMapper objectMapper;
    private final MessageProducer messageProducer;
    private final ProductStockService productStockService;
    private final ProductService productService;
    private final CompanyFeignClientService companyFeignClientService;
    private final UserFeignClientService userFeignClientService;
    private final NotiClientService notiClientService;
    private static final int STOCK_LOW_NOTIFICATION_NUM = 10;

    /* listener
     * 재고 감소 요청 process_product
     * 취소에 위한 재고 증가 요청 rollback_product
     *
     */
    @Override
    public void processMessage(String topic, String message) {
        log.info("Processing product message for topic: {}, message: {}", topic, message);

        // 토픽별로 주문 처리 로직을 분기
        switch (topic) {
            case "process_product":
                handleProcessProduct(message);
                break;
            case "rollback_product":
                handleRollbackProduct(message);
                break;
            default:
                log.warn("Unknown topic: {}", topic);
                throw new IllegalArgumentException("Unknown topic: " + topic);
        }
    }

    private void handleProcessProduct(String message) {
        log.info("Process Product received for , message: {}", message);
        ProcessProduct processProduct = null;

        try {
            processProduct = objectMapper.readValue(message, ProcessProduct.class);
            ProductStockDto productStockDto = new ProductStockDto();
            productStockDto.setAmount(processProduct.getOrderQuantity());
            productStockDto.setOrderId(processProduct.getOrderId());

            final int currentQuantity = productStockService.reduceProductStock(processProduct.getProductId(), productStockDto);

            if (currentQuantity <= STOCK_LOW_NOTIFICATION_NUM) {
                sendLowStockNotification(processProduct.getProductId(), currentQuantity);
            }
            sendCompleteProduct(processProduct.getProductId(), processProduct.getOrderId(),
                processProduct.getOrderQuantity(), processProduct.getOrderTotalPrice());
        } catch (IllegalArgumentException e) {
            if (processProduct != null) {
                sendErrorProduct(processProduct.getOrderId());
            }

            log.error("Error processing product: {}", e.getMessage());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    private void handleRollbackProduct(String message) {
        try {
            log.info("Rollback Product received for , message: {}", message);
            RollbackProduct rollbackProduct = objectMapper.readValue(message, RollbackProduct.class);

            productStockService.rollbackProduct(rollbackProduct.getOrderId(), rollbackProduct.getProductId(),
                rollbackProduct.getOrderQuantity());

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    /* send
     * 재고 차감 완료 후 메세지 전송
     * 주문 수량이 재고 수량보다 많을 경우 에러메시지
     * 주문 재고 10개이하 일경우 관리자에게 메시지 알림
     */
    public void sendCompleteProduct(UUID productId, UUID orderId, int orderQuantity, BigDecimal orderTotalPrice) {
        ProductResponseDto productResponseDto = productService.getProductById(productId);
        //재고수량 차감 완료 메시지 생성 및 주문 서버에 send
        CompleteProduct completeProduct = new CompleteProduct();
        completeProduct.setOrderId(orderId);
        completeProduct.setCompanyId(productResponseDto.getCompanyId());
        completeProduct.setOrderTotalPrice(orderTotalPrice);
        completeProduct.setOrderQuantity(orderQuantity);

        sendMessage("complete_product", completeProduct);
    }

    //주문 수량이 재고 수량보다 많을 경우 에러메시지 주문서버에 전달
    private void sendErrorProduct(UUID orderId) {
        ErrorProduct errorProduct = new ErrorProduct();
        errorProduct.setOrderId(orderId);
        errorProduct.setErrorMessage("재고수량이 부족합니다.");

        sendMessage("error_product", errorProduct);
    }

    //주문 재고 10개이하 일경우 메시지알림
    public void sendLowStockNotification(UUID productId, int currentQuantity) {
        //상품의 업체 조회
        final ProductResponseDto productResponseDto = productService.getProductById(productId);

        //업체의 관리자 조회 - 내부통신이용
        final CompanyResponse companyResponse = companyFeignClientService.getCompanyById(productResponseDto.getCompanyId());

        //슬랙 아이디 조회 - 내부통신
        final UserResponse user = userFeignClientService.getUserById(companyResponse.getCompanyManagerId());


        CreateSlackMessageRequest req = new CreateSlackMessageRequest();
        req.setReceiveSlackId(user.getSlackId());
        req.setReceiveUserId(user.getUserId());
        req.setSlackMessageStatus(SlackMessageStatus.READY.name());
        req.setSlackMessage("currentQuantity: " + currentQuantity + ", productId: " + productId + ", companyId: " + companyResponse.getCompanyId());
        req.setSentTime(DateTimeUtil.formatLocalDateTime(LocalDateTime.now()));
        req.setSlackMessageSenderId("coopang-bot");
        notiClientService.createSlackMessage(req);
    }

    //공통함수 : 메세지 보내기
    private void sendMessage(String topic, Object payload) {
        try {
            final String message = objectMapper.writeValueAsString(payload);
            messageProducer.sendMessage(topic, message);
        } catch (Exception e) {
            log.error("Error while sending message to topic {}: {}", topic, e.getMessage(), e);
        }
    }
}
