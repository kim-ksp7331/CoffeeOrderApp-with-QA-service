package orderapp.coffeeorder.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import orderapp.coffeeorder.member.entity.Member;
import orderapp.coffeeorder.validator.NotSpace;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MemberDTO {
    @Getter
    public static class Post {
        @Email
        @NotBlank
        private String email;
        @NotBlank(message = "회원 이름은 공백이 아니어야 합니다.")
        private String name;
        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$", message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다.")
        @NotBlank(message = "휴대폰 번호는 공백이 아니어야 합니다.")
        private String phone;

        @NotBlank(message = "비밀 번호는 공백이 아니어야 합니다.")
        private String password;
    }
    @Getter
    public static class Patch {
        @Setter
        private long memberId;
        @NotSpace(message = "회원 이름은 공백이 아니어야 합니다.")
        private String name;
        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$", message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다.")
        private String phone;
        private Member.MemberStatus memberStatus;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private long memberId;
        private String email;
        private String name;
        private String phone;
        private Member.MemberStatus memberStatus;
        private int stampCount;
    }
}
