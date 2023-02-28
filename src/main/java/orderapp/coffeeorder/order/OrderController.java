package orderapp.coffeeorder.order;

import orderapp.coffeeorder.order.entity.Order;
import orderapp.coffeeorder.order.mapper.OrderMapper;
import orderapp.coffeeorder.response.MultiResponseDTO;
import orderapp.coffeeorder.response.SingleResponseDTO;
import orderapp.coffeeorder.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {
    private final static String ORDER_DEFAULT_URL = "/orders";
    private final OrderService orderService;
    private final OrderMapper mapper;

    public OrderController(OrderService orderService, OrderMapper mapper) {
        this.orderService = orderService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> postOrder(@Valid @RequestBody OrderDTO.Post orderPostDTO) {
        Order order = orderService.createOrder(mapper.orderPostDTOToOrder(orderPostDTO));
        URI location = UriCreator.createUri(ORDER_DEFAULT_URL, order.getOrderId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{order-id}")
    public ResponseEntity<?> patchOrder(@PathVariable("order-id") @Positive long orderId,
                                        @Valid @RequestBody OrderDTO.Patch orderPatchDTO) {
        orderPatchDTO.setOrderId(orderId);
        Order order = orderService.updateOrder(mapper.orderPatchDTOToOrder(orderPatchDTO));
        OrderDTO.Response response = mapper.orderToOrderResponseDTO(order);
        return new ResponseEntity<>(new SingleResponseDTO<>(response), HttpStatus.OK);
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<?> getOrder(@PathVariable("order-id") @Positive long orderId) {
        Order order = orderService.findOrder(orderId);
        OrderDTO.Response response = mapper.orderToOrderResponseDTO(order);
        return new ResponseEntity<>(new SingleResponseDTO<>(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getOrders(@Positive int page, @Positive int size) {
        Page<Order> orderPage = orderService.findOrders(page - 1, size);
        List<Order> orders = orderPage.getContent();
        return new ResponseEntity<>(
                new MultiResponseDTO<>(mapper.ordersToOrderResponseDTOs(orders), orderPage), HttpStatus.OK);
    }

    @DeleteMapping("/{order-id}")
    public ResponseEntity<?> cancelOrder(@PathVariable("order-id") @Positive long orderId) {
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
