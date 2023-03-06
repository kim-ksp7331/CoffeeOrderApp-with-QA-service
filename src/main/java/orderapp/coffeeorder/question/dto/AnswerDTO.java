package orderapp.coffeeorder.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import orderapp.coffeeorder.question.entity.Answer;

import javax.validation.constraints.NotNull;

public class AnswerDTO {
    @Getter
    public static class Post {
        @Setter
        private long questionId;
        private String email;
        @NotNull
        private String content;
    }
    @Getter
    public static class Patch {
        @Setter
        private long questionId;
        private String email;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private long answerId;
        private long questionId;
        private String content;
        private Answer.AnswerStatus answerStatus;
    }
}
