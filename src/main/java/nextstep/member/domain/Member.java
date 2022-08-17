package nextstep.member.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Member {
    private static final int CHILDREN_AGE_MIN = 6;
    private static final int CHILDREN_AGE_MAX = 12;
    private static final int TEENAGE_AGE_MIN = 13;
    private static final int TEENAGE_AGE_MAX = 18;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private Integer age;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "MEMBER_ROLE",
            joinColumns = @JoinColumn(name = "id", referencedColumnName = "id")
    )
    @Column(name = "role")
    private List<String> roles;

    public Member() {
    }

    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.roles = List.of(RoleType.ROLE_MEMBER.name());
    }

    public Member(String email, String password, Integer age, List<String> roles) {
        this.email = email;
        this.password = password;
        this.age = age;
        this.roles = roles;
    }

    public boolean isChildren() {
        return CHILDREN_AGE_MIN <= age && age <= CHILDREN_AGE_MAX;
    }

    public boolean isTeenage() {
        return TEENAGE_AGE_MIN <= age && age <= TEENAGE_AGE_MAX;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }
}
