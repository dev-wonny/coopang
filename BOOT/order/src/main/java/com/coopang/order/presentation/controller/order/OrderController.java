package com.coopang.order.presentation.controller.order;


import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.apidata.application.order.enums.OrderStatusEnum;
import com.coopang.apidata.application.user.enums.UserRoleEnum;
import com.coopang.order.application.request.order.OrderDto;
import com.coopang.order.application.response.order.OrderResponseDto;
import com.coopang.order.application.service.message.OrderMessageService;
import com.coopang.order.application.service.order.OrderService;
import com.coopang.order.presentation.request.order.OrderGetAllConditionDto;
import com.coopang.order.presentation.request.order.OrderRequestDto;
import com.coopang.order.presentation.request.order.OrderSearchConditionDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "OrderContorller API", description = "OrderContorller API")
@Slf4j(topic = "OrderContorller")
@RestController
@RequestMapping("/orders/v1")
public class OrderController {

    private final ModelMapperConfig mapperConfig;
    private final OrderService orderService;
    private final OrderMessageService orderMessageService;

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";

    public OrderController(
            ModelMapperConfig mapperConfig,
            OrderService orderService,
            OrderMessageService orderMessageService
    ) {
        this.mapperConfig = mapperConfig;
        this.orderService = orderService;
        this.orderMessageService = orderMessageService;
    }

    // 주문 생성 //
    @PostMapping("/order")
    @Secured("ROLE_MASTER")
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        OrderDto orderDto = mapperConfig.strictMapper().map(orderRequestDto, OrderDto.class);
        OrderResponseDto orderResponseDto = orderService.createOrder(orderDto);
        // 메세지 보내기 <process_product>
        orderMessageService.sendProcessProduct(
                orderResponseDto.getOrderId(),
                orderResponseDto.getProductId(),
                orderResponseDto.getOrderQuantity(),
                orderResponseDto.getOrderTotalPrice()
        );
        // 결제 기록 요청
        orderMessageService.sendProcessPayment(orderResponseDto.getOrderId(),"SUCCESS");

        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    /*
    주문 단건 조회
    Todo : MASTER, CUSTOMER 용 만들기
    Todo : HUB_MANAGER 권한 체크 만들기
    Todo : COMPANY 권한 체크 만들기
     */
    @GetMapping("order/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable("orderId") UUID orderId) {
        OrderResponseDto orderResponseDto = orderService.findById(orderId);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.OK);
    }
    /*
    주문 전체 조회
    Todo : CUSTOMER 용 만들기
    Todo : HUB_MANAGER 용 만들기
    Todo : COMPANY 용 만들기
    Todo : MASTER 용 만들기
    * RequestParam startDate, endDate 생성하기
     */
    // Todo : CUSTOMER 용 만들기
    @PostMapping("/order/me/{userId}")
    @Secured({UserRoleEnum.Authority.MASTER,UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<Page<OrderResponseDto>> getAllByUserOrder(
            @PathVariable("userId") UUID userId,
            OrderGetAllConditionDto orderGetAllConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByUser(userId,orderGetAllConditionDto,pageable);

        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
    // Todo : HUB_MANAGER 용 만들기
    @PostMapping("/order/hub/{hubId}")
    @Secured({UserRoleEnum.Authority.MASTER,UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<Page<OrderResponseDto>> getAllByHubOrder(
            @PathVariable("hubId") UUID hubId,
            OrderGetAllConditionDto orderGetAllConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByHub(hubId,orderGetAllConditionDto,pageable);

        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
    // Todo : COMPANY 용 만들기
    @PostMapping("/order/CompanyId/{companyId}")
    @Secured({UserRoleEnum.Authority.MASTER,UserRoleEnum.Authority.HUB_MANAGER,UserRoleEnum.Authority.COMPANY})
    public ResponseEntity<Page<OrderResponseDto>> getAllByCompanyOrder(
            @PathVariable("companyId") UUID companyId,
            OrderGetAllConditionDto orderGetAllConditionDto,
            Pageable pageable
            ) {
        Page<OrderResponseDto> orders = orderService.findAllByCompany(companyId,orderGetAllConditionDto,pageable);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
    // Todo : MASTER 용 만들기
    @PostMapping("/order")
    @Secured(UserRoleEnum.Authority.MASTER)
    public ResponseEntity<Page<OrderResponseDto>> getAllByMasterOrder(
            OrderGetAllConditionDto orderGetAllConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByMaster(orderGetAllConditionDto,pageable);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
    /*
    주문 검색
    Todo : CUSTOMER 용 만들기
    Todo : HUB_MANAGER 용 만들기
    Todo : COMPANY 용 만들기
    Todo : MASTER 용 만들기
    * RequestParam startDate, endDate 생성하기
    * keyword 는 우선 orderId
     */
    // Todo : CUSTOMER 용 만들기
    @PostMapping("/order/me/{userId}/search")
    @Secured({UserRoleEnum.Authority.MASTER,UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<Page<OrderResponseDto>> getAllByUserSearchOrder(
            @PathVariable("userId") UUID userId,
            OrderSearchConditionDto orderSearchConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByUserSearch(userId,orderSearchConditionDto,pageable);

        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
    // Todo : HUB_MANAGER 용 만들기
    @PostMapping("/order/hub/{hubId}/search")
    @Secured({UserRoleEnum.Authority.MASTER,UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<Page<OrderResponseDto>> getAllByHubSearchOrder(
            @PathVariable("hubId") UUID hubId,
            OrderSearchConditionDto orderSearchConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByHubSearch(hubId,orderSearchConditionDto,pageable);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
    // Todo : COMPANY 용 만들기
    @PostMapping("/order/company/{companyId}/search")
    @Secured({UserRoleEnum.Authority.MASTER,UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<Page<OrderResponseDto>> getAllByCompanySearchOrder(
            @PathVariable("companyId") UUID companyId,
            OrderSearchConditionDto orderSearchConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByCompanySearch(companyId,orderSearchConditionDto,pageable);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
    // Todo : MASTER 용 만들기
    @PostMapping("/order/search")
    @Secured({UserRoleEnum.Authority.MASTER,UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<Page<OrderResponseDto>> getAllByMasterSearchOrder(
            OrderSearchConditionDto orderSearchConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByMasterSearch(orderSearchConditionDto,pageable);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }



    /*
    Todo : 주문 상태값 변경
    1. Ready -> Pending : 최초 주문 생성 -> 재고 감소 후 상태 변경
    2. Pending -> Shipped : 처음 주문 상태 -> 배송 등록 완료
    3. Shipped -> Deliveryed : 배송 등록 완료 -> 배달 완료
    4. Pending -> Canceled  : 처음 주문 상태 -> 주문 취소
     */
    // 4. Pending -> Canceled  : 처음 주문 상태 -> 주문 취소
    @PatchMapping("/order/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable("orderId") UUID orderId) {
        orderService.updateOrderStatus(orderId, OrderStatusEnum.CANCELED);
        final String message = "Order Cancelled";
        // 결제 취소 보내기
        orderMessageService.sendProcessPayment(orderId,"CANCEL");
        // 상품 롤백 보내기
        orderMessageService.sendRollbackProduct(orderId);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    /*
    Todo : 주문 삭제
    * 논리적 삭제
     */
}
