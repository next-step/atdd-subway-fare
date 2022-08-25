package nextstep.subway.domain.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FareChainType {
    BASIC_CHAIN("basicFareCalculator"),
    MEDIUM_DISTANCE_PASSENGER_CHAIN("mediumDistancePassengerFareCalculator"),
    LONG_DISTANCE_PASSENGER_CHAIN("longDistancePassengerFareCalculator");

    private final String name;

    public String implementation() {
        return this.name;
    }


}
