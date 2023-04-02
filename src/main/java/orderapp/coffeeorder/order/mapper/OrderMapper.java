package orderapp.coffeeorder.order.mapper;

import orderapp.coffeeorder.order.OrderDTO;
import orderapp.coffeeorder.order.entity.Order;
import orderapp.coffeeorder.order.entity.OrderCoffee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper{
    @Mapping(target = "coffee.coffeeId", source = "coffeeId")
    OrderCoffee orderPostDTOOrderCoffeeToOrderCoffee(OrderDTO.Post.OrderCoffee postOrderCoffee);

    @Mapping(target = "member.memberId", source = "memberId")
    Order orderPostDTOToOrder(OrderDTO.Post orderPostDTO);
    @Mapping(target = "member.memberId", source = "memberId")
    Order orderPatchDTOToOrder(OrderDTO.Patch orderPatchDTO);

    @Mapping(source = "coffee", target = ".")
    OrderDTO.Response.OrderCoffee orderCoffeeToResponseOrderCoffee(OrderCoffee orderCoffee);

    @Mapping(source = "member.memberId", target = "memberId")
    OrderDTO.Response orderToOrderResponseDTO(Order order);

    List<OrderDTO.Response> ordersToOrderResponseDTOs(List<Order> orders);

}
