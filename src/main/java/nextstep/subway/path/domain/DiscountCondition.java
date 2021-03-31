package nextstep.subway.path.domain;

public class DiscountCondition< T > {
    public T t;

    public DiscountCondition(T t) {
        this.t = t;
    }

    public T getCondition() {
        return t;
    }
}
