package nextstep.core.subway.fare.application;

import nextstep.core.subway.path.application.FareCalculationContext;

public interface FareCalculatePolicy {
    int apply(FareCalculationContext context);
}