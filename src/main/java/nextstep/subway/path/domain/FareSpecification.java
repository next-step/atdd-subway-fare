package nextstep.subway.path.domain;

import nextstep.subway.path.domain.valueobject.Age;

public class FareSpecification {
    public Age age;

    public FareSpecification() {
        this(null);
    }

    public FareSpecification(Age age) {
        this.age = age;
    }

    public Age getAge() {
        return age;
    }

    public boolean hasAge() {
        return (age != null) ? true : false;
    }
}
