package nextstep.subway.domain.farepolicy.base;

import nextstep.subway.domain.map.Path;

public interface FarePolicy {
    int calculate(Path path);
}
