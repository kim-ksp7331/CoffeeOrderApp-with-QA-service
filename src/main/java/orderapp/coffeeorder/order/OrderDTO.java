package orderapp.coffeeorder.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import orderapp.coffeeorder.coffee.Coffee;
import orderapp.coffeeorder.member.entity.Member;
import orderapp.coffeeorder.order.entity.Order;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    @Getter
    public static class Post {
        @Positive
        private long memberId;
        @Valid
        private List<OrderCoffee> orderCoffees;

        public Member getMember() {
            Member member = new Member();
            member.setMemberId(memberId);
            return member;
        }

        @Getter
        public static class OrderCoffee {
            @Positive
            private long coffeeId;
            @Positive
            private int quantity;

            public Coffee getCoffee() {
                Coffee coffee = new Coffee();
                coffee.setCoffeeId(coffeeId);
                return coffee;
            }
        }
    }

    @Getter
    public static class Patch {
        @Setter
        private long orderId;
        private Order.OrderStatus orderStatus;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private long orderId;
        private long memberId;
        private Order.OrderStatus orderStatus;
        private List<OrderCoffee> orderCoffees;
        private LocalDateTime createdAt;

        @Getter
        @AllArgsConstructor
        public static class OrderCoffee {
            private long coffeeId;
            private int quantity;
            private String korName;
            private String engName;
            private int price;
        }
    }
}
