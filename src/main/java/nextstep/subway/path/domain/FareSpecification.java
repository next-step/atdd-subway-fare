package nextstep.subway.path.domain;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.valueobject.Age;

import javax.swing.text.html.Option;
import java.util.Optional;

public class FareSpecification {
    public Age age;

    public FareSpecification() {
        this(null);
    }

    public FareSpecification(LoginMember member) {
        age = Optional.ofNullable(member)
                .map(mem -> Age.of(mem.getAge()))
                .orElse(null);
    }

    public Age getAge() {
        return age;
    }

    public boolean hasAge() {
        return (age != null) ? true : false;
    }
}
