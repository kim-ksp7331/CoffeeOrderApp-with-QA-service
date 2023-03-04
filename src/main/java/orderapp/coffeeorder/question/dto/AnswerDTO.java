package orderapp.coffeeorder.question.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AnswerDTO {
    @Getter
    public static class Post {
        @Setter
        private long questionId;
        private String email;
        @NotNull
        private String content;
    }
}
