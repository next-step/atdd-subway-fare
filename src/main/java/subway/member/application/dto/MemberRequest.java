package subway.member.application.dto;

import lombok.Builder;
import lombok.Getter;
import subway.member.domain.Member;
import subway.member.domain.RoleType;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class MemberRequest {

    private static final String PASSWORD_NOT_BLANK_MESSAGE = "패스워드는 필수 입니다.";
    private static final String AGE_MIN_MESSAGE = "나이는 필수 값 입니다.";

    @Email
    private String email;

    @NotNull(message = PASSWORD_NOT_BLANK_MESSAGE)
    private String password;

    @Min(value = 1, message = AGE_MIN_MESSAGE)
    private Integer age;

    public Member to() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .age(this.age)
                .build();
    }

    public Member toInit() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .age(this.age)
                .role(RoleType.ROLE_MEMBER)
                .build();
    }
}
