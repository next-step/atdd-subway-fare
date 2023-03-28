package nextstep.subway.fixture;


import static nextstep.subway.fixture.FieldFixture.식별자_아이디;
import static nextstep.subway.fixture.FieldFixture.회원_비밀번호;
import static nextstep.subway.fixture.FieldFixture.회원_이메일;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;
import org.springframework.test.util.ReflectionTestUtils;

public enum MemberFixture {
    성인_회원_ALEX("dev.gibeom@gmail.com", "alexpassword", 27, RoleType.ROLE_MEMBER),
    청소년_회원_JADE("jade@gmail.com", "jadepassword", 17, RoleType.ROLE_MEMBER),
    어린이_회원_BEOM("kpmyung@gmail.com", "beompassword", 7, RoleType.ROLE_MEMBER),
    회원_ALEX_수정("dev.gibeom@gmail.com", "drowssap", 72, RoleType.ROLE_MEMBER),
    비회원("ImnotMember@gmail.com", "xxxxxxx", 72, RoleType.ROLE_MEMBER),
    관리자_ADMIN("admin@gmail.com", "adminpassword", 30, RoleType.ROLE_ADMIN),
    ;

    private final String email;
    private final String password;
    private final Integer age;
    private final List<String> roles;

    MemberFixture(String email, String password, Integer age, RoleType... roles) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.roles = Arrays.stream(roles)
                .map(Enum::name)
                .collect(Collectors.toList());
    }


    public String 이메일() {
        return email;
    }

    public String 비밀번호() {
        return password;
    }

    public Integer 나이() {
        return age;
    }

    public List<String> 역할_목록() {
        return roles;
    }

    public Map<String, String> 로그인_요청_데이터_생성() {
        Map<String, String> params = new HashMap<>();
        params.put(회원_이메일.필드명(), 이메일());
        params.put(회원_비밀번호.필드명(), 비밀번호());
        return params;
    }

    public Member 엔티티_생성() {
        return new Member(이메일(), 비밀번호(), 나이(), 역할_목록());
    }

    public Member 엔티티_생성(Long id) {
        Member member = new Member(이메일(), 비밀번호(), 나이(), 역할_목록());
        ReflectionTestUtils.setField(member, 식별자_아이디.필드명(), id);
        return member;
    }
}
