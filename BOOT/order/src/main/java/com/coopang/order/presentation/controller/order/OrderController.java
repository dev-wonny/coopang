package com.coopang.order.presentation.controller.order;


import com.coopang.apiconfig.mapper.ModelMapperConfig;
import com.coopang.apidata.application.order.enums.OrderStatusEnum;
import com.coopang.coredata.user.enums.UserRoleEnum;
import com.coopang.order.application.request.order.OrderDto;
import com.coopang.order.application.response.order.OrderResponseDto;
import com.coopang.order.application.service.message.order.OrderMessageService;
import com.coopang.order.application.service.order.OrderService;
import com.coopang.order.presentation.request.order.OrderGetAllConditionDto;
import com.coopang.order.presentation.request.order.OrderRequestDto;
import com.coopang.order.presentation.request.order.OrderSearchConditionDto;
import com.coopang.order.presentation.request.order.UpdateOrderRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ID;
import static com.coopang.coredata.user.constants.HeaderConstants.HEADER_USER_ROLE;

@Tag(name = "OrderContorller API", description = "OrderContorller API")
@Slf4j(topic = "OrderContorller")
@RestController
@RequestMapping("/orders/v1")
public class OrderController {

    private final ModelMapperConfig mapperConfig;
    private final OrderService orderService;
    private final OrderMessageService orderMessageService;

    public OrderController(
            ModelMapperConfig mapperConfig,
            OrderService orderService,
            OrderMessageService orderMessageService
    ) {
        this.mapperConfig = mapperConfig;
        this.orderService = orderService;
        this.orderMessageService = orderMessageService;
    }

    /*
    주문 생성
    최초 생성 : READY
    수량 감소 & 결제 완료 : PENDING
     */
    @PostMapping("/order")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.CUSTOMER, UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto
            , @RequestHeader(HEADER_USER_ID) String userIdHeader
    ) {
        final UUID userId = UUID.fromString(userIdHeader);
        final OrderDto orderDto = mapperConfig.strictMapper().map(orderRequestDto, OrderDto.class);
        OrderResponseDto orderResponseDto = orderService.createOrder(userId, orderDto);
        // 메세지 보내기 <process_product>
        orderMessageService.sendProcessProduct(
                orderResponseDto.getOrderId()
                , orderResponseDto.getProductId()
                , orderResponseDto.getOrderQuantity()
                , orderResponseDto.getOrderTotalPrice()
        );
        // 결제 기록 요청
        orderMessageService.sendCompletePayment(
                orderResponseDto.getOrderId()
                , orderRequestDto.getPgPaymentId()
                , orderResponseDto.getOrderTotalPrice()
                , orderRequestDto.getPaymentMethod()
        );
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("order/{orderId}")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.CUSTOMER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.COMPANY})
    public ResponseEntity<OrderResponseDto> getOrder(
            @PathVariable("orderId") UUID orderId
            , @RequestParam(value = "hubId", required = false) UUID hubId
            , @RequestParam(value = "companyId", required = false) UUID companyId
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
            , @RequestHeader(HEADER_USER_ID) String userIdHeader
    ) {
        OrderResponseDto orderInfo;
        final UUID userId = UUID.fromString(userIdHeader);
        if (UserRoleEnum.isMaster(roleHeader)) {
            orderInfo = orderService.findById(orderId);
        } else if (UserRoleEnum.isCompany(roleHeader)) {
            orderInfo = orderService.findCompanyById(companyId,orderId);
        } else if (UserRoleEnum.isHubManager(roleHeader)) {
            orderInfo = orderService.findHubById(hubId,orderId);
        } else {
            orderInfo = orderService.findUserById(userId, orderId);
        }
        return new ResponseEntity<>(orderInfo, HttpStatus.OK);
    }

    // 1. CUSTOMER 용 만들기
    @GetMapping("/order/me/{userId}")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<Page<OrderResponseDto>> getAllByUserOrder(
            @PathVariable("userId") UUID userId,
            @RequestParam OrderGetAllConditionDto orderGetAllConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByUser(userId, orderGetAllConditionDto, pageable);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 2. HUB_MANAGER 용 만들기
    @GetMapping("/order/hub/{hubId}")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<Page<OrderResponseDto>> getAllByHubOrder(
            @PathVariable("hubId") UUID hubId,
            @RequestParam OrderGetAllConditionDto orderGetAllConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByHub(hubId, orderGetAllConditionDto, pageable);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 3. COMPANY 용 만들기
    @GetMapping("/order/CompanyId/{companyId}")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.COMPANY})
    public ResponseEntity<Page<OrderResponseDto>> getAllByCompanyOrder(
            @PathVariable("companyId") UUID companyId,
            @RequestParam OrderGetAllConditionDto orderGetAllConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByCompany(companyId, orderGetAllConditionDto, pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 4. MASTER 용 만들기
    @GetMapping("/order")
    @Secured(UserRoleEnum.Authority.MASTER)
    public ResponseEntity<Page<OrderResponseDto>> getAllByMasterOrder(
            @RequestParam OrderGetAllConditionDto orderGetAllConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByMaster(orderGetAllConditionDto, pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 1. CUSTOMER 용 만들기
    @PostMapping("/order/me/{userId}/search")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<Page<OrderResponseDto>> getAllByUserSearchOrder(
            @PathVariable("userId") UUID userId,
            @RequestBody OrderSearchConditionDto orderSearchConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByUserSearch(userId, orderSearchConditionDto, pageable);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 2. HUB_MANAGER 용 만들기
    @PostMapping("/order/hub/{hubId}/search")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<Page<OrderResponseDto>> getAllByHubSearchOrder(
            @PathVariable("hubId") UUID hubId,
            @RequestBody OrderSearchConditionDto orderSearchConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByHubSearch(hubId, orderSearchConditionDto, pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 5. COMPANY 용 만들기
    @PostMapping("/order/company/{companyId}/search")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<Page<OrderResponseDto>> getAllByCompanySearchOrder(
            @PathVariable("companyId") UUID companyId,
            @RequestBody OrderSearchConditionDto orderSearchConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByCompanySearch(companyId, orderSearchConditionDto, pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // 4. MASTER 용 만들기
    @PostMapping("/order/search")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<Page<OrderResponseDto>> getAllByMasterSearchOrder(
            @RequestBody OrderSearchConditionDto orderSearchConditionDto,
            Pageable pageable
    ) {
        Page<OrderResponseDto> orders = orderService.findAllByMasterSearch(orderSearchConditionDto, pageable);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /*
    주문 수정
    * 배송지 수정만 가능
    * 수량변경을 할려면 다시 결제가 일어나기 때문에 수정이 불가능
     */
    @PutMapping("/order/{orderId}")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<OrderResponseDto> updateOrder(
            @PathVariable("orderId") UUID orderId
            , @RequestParam(value = "hubId", required = false) UUID hubId
            , @Valid @RequestBody UpdateOrderRequestDto updateOrderRequestDto
    ) {
        final OrderDto orderDto = mapperConfig.strictMapper().map(updateOrderRequestDto, OrderDto.class);
        orderService.updateOrder(orderId, orderDto);
        final OrderResponseDto orderInfo = orderService.findById(orderId);
        return new ResponseEntity<>(orderInfo, HttpStatus.OK);
    }

    // 1. Ready -> Pending : 최초 주문 생성 -> 재고 감소 후 상태 변경
    @PatchMapping("/order/{orderId}/pending")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<Void> pendingOrder(
            @PathVariable("orderId") UUID orderId
            , @RequestParam(value = "hubId", required = false) UUID hubId
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
    ) {
        if (UserRoleEnum.isMaster(roleHeader)){
            orderService.updateOrderStatus(orderId, OrderStatusEnum.PENDING);
        } else {
            orderService.updateOrderStatusByHub(hubId, orderId, OrderStatusEnum.PENDING);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 2. Pending -> Shipped : 처음 주문 상태 -> 배송 등록 완료
    @PatchMapping("/order/{orderId}/shipped")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<Void> shippedOrder(
            @PathVariable("orderId") UUID orderId
            , @RequestParam(value = "hubId", required = false) UUID hubId
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
    ) {
        if (UserRoleEnum.isMaster(roleHeader)){
            orderService.updateOrderStatus(orderId, OrderStatusEnum.SHIPPED);
        } else {
            orderService.updateOrderStatusByHub(hubId, orderId, OrderStatusEnum.SHIPPED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 3. Shipped -> Delivered : 배송 등록 완료 -> 배달 완료
    @PatchMapping("/order/{orderId}/delivered")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<Void> deliveredOrder(
            @PathVariable("orderId") UUID orderId
            , @RequestParam(value = "hubId", required = false) UUID hubId
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
    ) {
        if (UserRoleEnum.isMaster(roleHeader)){
            orderService.updateOrderStatus(orderId, OrderStatusEnum.DELIVERED);
        } else {
            orderService.updateOrderStatusByHub(hubId, orderId, OrderStatusEnum.DELIVERED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 4. Pending -> Canceled  : 처음 주문 상태 -> 주문 취소
    @PatchMapping("/order/{orderId}")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER, UserRoleEnum.Authority.CUSTOMER})
    public ResponseEntity<String> cancelOrder(
            @PathVariable("orderId") UUID orderId
            , @RequestParam(value = "hubId", required = false) UUID hubId
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
            , @RequestHeader(HEADER_USER_ID) String userIdHeader
    ) {
        final UUID userId = UUID.fromString(userIdHeader);
        final String message = "Order Cancelled";
        if (UserRoleEnum.isMaster(roleHeader)){
            orderService.updateOrderStatus(orderId, OrderStatusEnum.CANCELED);
        } else if (UserRoleEnum.isCustomer(roleHeader)){
            orderService.updateOrderStatusByCustomer(hubId, orderId, OrderStatusEnum.CANCELED);
        } else {
            orderService.updateOrderStatusByHub(userId, orderId, OrderStatusEnum.CANCELED);
        }
        // 결제 취소 보내기
        orderMessageService.sendCancelPayment(orderId);
        // 상품 롤백 보내기
        orderMessageService.sendRollbackProduct(orderId);
        // 배송 취소 보내기
        orderMessageService.sendCancelDelivery(orderId);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /*
    주문 삭제
    * 논리적 삭제
     */
    //단일 주문 삭제 (논리적 삭제)
    @DeleteMapping("/order/{orderId}")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.HUB_MANAGER})
    public ResponseEntity<Void> deleteOrder(
            @PathVariable UUID orderId
            , @RequestParam(value = "hubId", required = false) UUID hubId
            , @RequestHeader(HEADER_USER_ROLE) String roleHeader
    ) {
        if (UserRoleEnum.isMaster(roleHeader)){
            orderService.deleteOrder(orderId);
        } else {
            orderService.deleteOrderByHubManager(hubId,orderId);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // feginclient
    @GetMapping("/order/list")
    @Secured({UserRoleEnum.Authority.MASTER, UserRoleEnum.Authority.SERVER})
    public ResponseEntity<List<OrderResponseDto>> getOrderList() {
        final List<OrderResponseDto> orders = orderService.getOrderList();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
