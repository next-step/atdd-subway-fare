package nextstep.member.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Member {

    private static final int CHILDREN_MIN_AGE = 6;
    private static final int CHILDREN_MAX_AGE = 13;
    private static final int TEENAGER_MIN_AGE = 13;
    private static final int TEENAGER_MAX_AGE = 19;

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

    protected Member() {
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

    public boolean isChildren() {
        return age >= CHILDREN_MIN_AGE && age < CHILDREN_MAX_AGE;
    }

    public boolean isTeenager() {
        return age >= TEENAGER_MIN_AGE && age < TEENAGER_MAX_AGE;
    }
}
