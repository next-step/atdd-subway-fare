package nextstep.subway.path.domain;

public interface AgeDiscountSpecification {

    int discount(int fare);

    boolean apply(int age);
}
