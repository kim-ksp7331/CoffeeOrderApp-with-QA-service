package orderapp.coffeeorder.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member Not Found"),
    MEMBER_EXISTS(409, "Member exists"),
    COFFEE_NOT_FOUND(404, "Coffee Not Found"),
    COFFEE_CODE_EXISTS(409, "Coffee Code Exists"),
    ORDER_NOT_FOUND(404, "Order Not Found"),
    CANNOT_CHANGE_ORDER(403, "Order Cannot Change"),
    NOT_IMPLEMENTATION(501, "Not Implementation");
    @Getter
    private int status;
    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
