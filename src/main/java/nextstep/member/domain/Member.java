package nextstep.member.domain;

import nextstep.subway.domain.FareAgeGroup;

import javax.persistence.*;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private Integer age;

    public Member() {
    }

    public Member(String email) {
        this(email, "", 0);
    }

    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
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

    public void update(Member member) {
        this.email = member.email;
        this.password = member.password;
        this.age = member.age;
    }

    public long discountExtraFare(long fare) {
        FareAgeGroup fareAgeGroup = FareAgeGroup.of(age);
        return fareAgeGroup.calculateFare(fare);
    }

}
