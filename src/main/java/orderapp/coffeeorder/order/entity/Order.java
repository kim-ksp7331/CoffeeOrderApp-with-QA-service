package orderapp.coffeeorder.order.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import orderapp.coffeeorder.audit.Auditable;
import orderapp.coffeeorder.member.entity.Member;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "ORDERS")
public class Order extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrderStatus orderStatus = OrderStatus.ORDER_REQUEST;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderCoffee> orderCoffees;

    public void addMember(Member member) {
        this.member = member;
    }

    public void addOrderCoffee(OrderCoffee orderCoffee) {
        orderCoffees.add(orderCoffee);
    }

    public enum OrderStatus {
        ORDER_REQUEST(1, "주문 요청"),
        ORDER_CONFIRM(2, "주문 확정"),
        ORDER_COMPLETE(3, "주문 처리 완료"),
        ORDER_CANCEL(4, "주문 취소");
        @Getter
        private int stepNumber;
        @Getter
        private String stepDescription;

        OrderStatus(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }
    }
}
