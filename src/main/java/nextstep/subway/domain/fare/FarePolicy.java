package nextstep.subway.domain.fare;

import nextstep.subway.domain.Path;

public interface FarePolicy {
    int fare(Path path);
}
