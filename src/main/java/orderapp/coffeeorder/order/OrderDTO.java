package orderapp.coffeeorder.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import orderapp.coffeeorder.order.entity.Order;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

public class OrderDTO {
    @Getter
    public static class Post {
        @Positive
        @Setter
        private Long memberId;
        @Valid
        private List<OrderCoffee> orderCoffees;
        @Getter
        public static class OrderCoffee {
            @Positive
            private long coffeeId;
            @Positive
            private int quantity;
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
