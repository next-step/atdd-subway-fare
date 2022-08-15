package nextstep.subway.domain.policy;

import nextstep.subway.domain.Path;

public interface BasicFarePolicy {
    int calculate(int age, int fare, int distance, Path path);
}
