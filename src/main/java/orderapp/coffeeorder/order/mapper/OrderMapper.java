package orderapp.coffeeorder.order.mapper;

import orderapp.coffeeorder.order.OrderDTO;
import orderapp.coffeeorder.order.entity.Order;
import orderapp.coffeeorder.order.entity.OrderCoffee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order orderPostDTOToOrder(OrderDTO.Post orderPostDTO);

    Order orderPatchDTOToOrder(OrderDTO.Patch orderPatchDTO);

    @Mapping(source = "coffee.coffeeId", target = "coffeeId")
    @Mapping(source = "coffee.korName", target = "korName")
    @Mapping(source = "coffee.engName", target = "engName")
    @Mapping(source = "coffee.price", target = "price")
    OrderDTO.Response.OrderCoffee orderCoffeeToResponseOrderCoffee(OrderCoffee orderCoffee);

    @Mapping(source = "member.memberId", target = "memberId")
    OrderDTO.Response orderToOrderResponseDTO(Order order);

    List<OrderDTO.Response> ordersToOrderResponseDTOs(List<Order> orders);


}
