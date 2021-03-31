package nextstep.subway.path.domain.valueobject;

import java.util.Objects;

public class Age {
    private int age;

    private Age(int age) {
        this.age = age;
    }

    public static Age of(int age) {
        return new Age(age);
    }

    public static int parseInt(Age age) {
        return age.getAge();
    }

    private int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Age age1 = (Age) o;
        return age == age1.age;
    }

    @Override
    public int hashCode() {
        return Objects.hash(age);
    }
}
