package nextstep.subway.path.domain.policy.fare;

import nextstep.subway.path.domain.Path;

/**
 * 요금 정책
 * - path에 대한 요금을 계산한다.
 */
public interface FarePolicy {
    int calculateFare(Path path);
}
