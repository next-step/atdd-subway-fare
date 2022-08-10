package nextstep.subway.domain;

import nextstep.subway.domain.fare.BasicStrategy;
import nextstep.subway.domain.fare.FareStrategy;
import nextstep.subway.domain.fare.LongStrategy;
import nextstep.subway.domain.fare.MiddleStrategy;

import java.util.function.Supplier;

public enum FareType {
    BASIC_DISTANCE(() -> new BasicStrategy()),
    MIDDLE_DISTANCE(() -> new MiddleStrategy()),
    LONG_DISTANCE(() -> new LongStrategy());

    private static final String INVALID_DISTANCE_EXCEPTION = "거리는 양수이여야만 합니다.";

    private final Supplier<FareStrategy> strategySupplier;

    FareType(Supplier<FareStrategy> strategySupplier) {
        this.strategySupplier = strategySupplier;
    }

    public static FareStrategy findStrategy(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException(INVALID_DISTANCE_EXCEPTION);
        } else if (distance <= 10) {
            return BASIC_DISTANCE.strategySupplier.get();
        } else if (distance <= 50) {
            return MIDDLE_DISTANCE.strategySupplier.get();
        } else {
            return LONG_DISTANCE.strategySupplier.get();
        }
    }
}
