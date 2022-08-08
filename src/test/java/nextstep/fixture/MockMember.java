package nextstep.fixture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum MockMember {
    ADMIN("admin@email.com", "password", 20, RoleType.ROLE_ADMIN),
    MEMBER("member@email.com", "password", 20, RoleType.ROLE_MEMBER),
    GUEST("user@user.com", "password", 19, RoleType.ROLE_MEMBER),
    TEENAGER("teenager@user.com", "password", 13, RoleType.ROLE_MEMBER),
    CHILD("child@user.com", "password", 6, RoleType.ROLE_MEMBER)
    ;

    private final String email;
    private final String password;
    private final int age;
    private final List<RoleType> roleTypes;

    MockMember(String email, String password, int age, RoleType... roleTypes) {
        this(email, password, age, List.of(roleTypes));
    }


    public static List<Member> getAllMembers() {
        return Arrays.stream(MockMember.values())
                .map(MockMember::toMember)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<String> getAuthorities() {
        return roleTypes.stream()
                .map(Enum::name)
                .collect(Collectors.toUnmodifiableList());
    }

    public Member toMember() {
        return new Member(
                this.email,
                this.password,
                this.age,
                getAuthorities()
        );
    }
}
