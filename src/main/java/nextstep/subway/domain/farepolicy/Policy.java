package nextstep.subway.domain.farepolicy;

public interface Policy {
    /**
     * 요금을 입력받고, 해당 정책이 반영된 요금을 반환한다.
     */
    int calculate(int fare);
}
