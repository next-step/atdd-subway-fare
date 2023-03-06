package nextstep.subway.domain;

import lombok.Getter;

@Getter
public enum DistanceByFare {

    DEFAULT_DISTANCE(10),

    OVER_BETWEEN_TEN_AND_FIFTY(50),

    STANDARD_DISTANCE_OVER_BETWEEN_TEN_AND_FIFTY(5),

    STANDARD_FARE_DISTANCE_OVER_FIFTY(8);

    private final int value;

    DistanceByFare(int value) {
        this.value = value;
    }
}
