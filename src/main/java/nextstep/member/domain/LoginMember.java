package nextstep.member.domain;

public class LoginMember {

    public static final LoginMember GUEST = new LoginMember();

    private Long memberId;
    private String email;
    private Integer age;

    private LoginMember() {}

    public LoginMember(final Long memberId, final String email, final Integer age) {
        this.memberId = memberId;
        this.email = email;
        this.age = age;
    }

    public static LoginMember from(final Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    public boolean isGuest() {
        return this == GUEST;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
