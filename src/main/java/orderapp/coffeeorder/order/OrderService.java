package orderapp.coffeeorder.order;

import lombok.RequiredArgsConstructor;
import orderapp.coffeeorder.coffee.CoffeeService;
import orderapp.coffeeorder.exception.BusinessLogicException;
import orderapp.coffeeorder.exception.ExceptionCode;
import orderapp.coffeeorder.member.MemberService;
import orderapp.coffeeorder.member.entity.Member;
import orderapp.coffeeorder.member.entity.Stamp;
import orderapp.coffeeorder.order.entity.Order;
import orderapp.coffeeorder.utils.CustomBeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final CoffeeService coffeeService;
    private final CustomBeanUtils<Order> beanUtils;


    public Order createOrder(Order order) {
        Member findMember = verifyOrder(order);
        order.getOrderCoffees().stream().forEach(orderCoffee -> orderCoffee.setOrder(order));
        addStampCount(order, findMember);
        return orderRepository.save(order);
    }

    private Member verifyOrder(Order order) {
        Member member = memberService.findVerifiedMember(order.getMember().getMemberId());
        order.getOrderCoffees().stream()
                .forEach(orderCoffee -> coffeeService.findVerifiedCoffee(orderCoffee.getCoffee().getCoffeeId()));
        return member;
    }

    private void addStampCount(Order order, Member findMember) {
        Stamp stamp = findMember.getStamp();
        int totalCoffeeQuantity = order.getOrderCoffees().stream()
                .mapToInt(orderCoffee -> orderCoffee.getQuantity()).sum();
        stamp.setStampCount(stamp.getStampCount() + totalCoffeeQuantity);
    }

    public Order updateOrder(Order order) {
        Order findOrder = findVerifiedOrder(order.getOrderId());
        verifyAccessibleMember(order.getMember().getMemberId(), findOrder.getMember().getMemberId());
        Order updatedOrder = beanUtils.copyNonNullProperties(order, findOrder);
        return orderRepository.save(updatedOrder);
    }

    public Order findOrder(long orderId, long memberId) {
        Order findOrder = findVerifiedOrder(orderId);
        verifyAccessibleMember(memberId, findOrder.getMember().getMemberId());
        return findOrder;
    }

    public Page<Order> findOrders(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("orderId").descending());
        return orderRepository.findAll(pageRequest);
    }

    public void cancelOrder(long orderId, long memberId) {
        Order findOrder = findVerifiedOrder(orderId);
        verifyAccessibleMember(memberId, findOrder.getMember().getMemberId());
        if (findOrder.getOrderStatus().getStepNumber() >= 2) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_ORDER);
        }
        findOrder.setOrderStatus(Order.OrderStatus.ORDER_CANCEL);
        orderRepository.save(findOrder);
    }

    public Order findVerifiedOrder(long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
    }
    private void verifyAccessibleMember(long memberId, long findMemberId) {
        if (memberId != findMemberId) {
            throw new AccessDeniedException("Access Denied");
        }
    }
}
