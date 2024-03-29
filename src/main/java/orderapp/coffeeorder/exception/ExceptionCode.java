package orderapp.coffeeorder.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member Not Found"),
    MEMBER_EXISTS(409, "Member exists"),
    COFFEE_NOT_FOUND(404, "Coffee Not Found"),
    COFFEE_CODE_EXISTS(409, "Coffee Code Exists"),
    ORDER_NOT_FOUND(404, "Order Not Found"),
    CANNOT_CHANGE_ORDER(403, "Order Cannot Change"),
    QUESTION_NOT_FOUND(404, "Question Not Found"),
    QUESTION_DELETED(404, "Question Deleted"),
    CANNOT_DELETE_QUESTION(403, "Cannot Delete Question"),
    CANNOT_DELETE_ANSWER(403, "Cannot Delete Answer"),
    ANSWER_NOT_FOUND(404, "Answer Not Found"),
    ANSWER_EXISTS(409, "Answer Exists"),
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
