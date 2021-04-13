package nextstep.subway.path.domain.fare.age;

public interface AgeChain {

    boolean findAgeGroup(int age);

    int calculate(int fare);
}
