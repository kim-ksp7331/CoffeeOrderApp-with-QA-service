package orderapp.coffeeorder.coffee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import orderapp.coffeeorder.validator.NotSpace;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CoffeeDTO {
    @Getter
    public static class Post {
        @NotBlank(message = "커피명(한글)은 공백이 아니어야 합니다.")
        private String korName;
        @NotBlank(message = "커피명(영문)은 공백이 아니어야 합니다.")
        @Pattern(regexp = "^[A-Za-z]+(\\s?[A-Za-z]+)*$", message = "커피명(영문)은 영문이어야 합니다(단어 사이 공백 한 칸 가능).")
        private String engName;
        @Range(min = 100, max = 50000)
        private int price;
        @NotBlank
        @Pattern(regexp = "^[A-za-z]{3}$", message = "커피 코드는 3자리 영문이어야 합니다.")
        private String coffeeCode;
    }
    @Getter
    public static class Patch {
        @Setter
        private long coffeeId;
        @NotSpace(message = "커피명(한글)은 공백이 아니어야 합니다.")
        private String korName;
        @Pattern(regexp = "^[A-Za-z]+(\\s?[A-Za-z]+)*$", message = "커피명(영문)은 영문이어야 합니다(단어 사이 공백 한 칸 가능).")
        private String engName;
        @Range(min = 100, max = 50000)
        private Integer price;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private long coffeeId;
        private String korName;
        private String engName;
        private Integer price;

    }
}
