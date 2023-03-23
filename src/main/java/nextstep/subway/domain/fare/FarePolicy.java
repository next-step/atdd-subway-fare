package nextstep.subway.domain.fare;

/**
 * 책임 연쇄 패턴을 사용한 요금 정책 체이닝
 */
public interface FarePolicy {

    void setNextPolicyChain(FarePolicy farePolicy);

    int calculateFare(int distance);
}
