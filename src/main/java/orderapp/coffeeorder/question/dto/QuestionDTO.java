package orderapp.coffeeorder.question.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import orderapp.coffeeorder.question.entity.Question;
import orderapp.coffeeorder.validator.NotSpace;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class QuestionDTO {
    @Getter
    public static class Post {
        @Positive
        private int memberId;
        @NotBlank(message = "제목은 공백이 아니어야 합니다.")
        private String title;
        @NotBlank(message = "내용은 공백이 아니어야 합니다.")
        private String content;
        private Question.QuestionAccess questionAccess;
    }

    @Getter
    public static class Patch {
        private long questionId;
        @Positive
        private int memberId;
        @NotSpace(message = "제목은 공백이 아니어야 합니다.")
        private String title;
        @NotSpace(message = "내용은 공백이 아니어야 합니다.")
        private String content;
        private Question.QuestionAccess questionAccess;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private long questionId;
        private String title;
        private String content;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String answer;
        private String author;
        private Question.QuestionStatus questionStatus;
        private Question.QuestionAccess questionAccess;

    }
}