package nextstep.subway.domain.farepolicy;

import java.util.List;

import nextstep.subway.domain.Path;

public interface FarePolicy {
    int calculate(Path path);
}
