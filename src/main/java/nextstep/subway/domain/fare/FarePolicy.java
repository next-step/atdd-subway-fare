package nextstep.subway.domain.fare;

import nextstep.subway.domain.Sections;

public interface FarePolicy {
    long calculateOverFare(Sections sections);
}
