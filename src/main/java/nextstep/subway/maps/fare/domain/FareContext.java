package nextstep.subway.maps.fare.domain;

import nextstep.subway.maps.map.domain.SubwayPath;

public class FareContext {
    public static final int DEFAULT_FARE = 1250;
    private final Fare fare;
    private final SubwayPath subwayPath;


    public FareContext(SubwayPath subwayPath) {
        this.fare = new Fare(DEFAULT_FARE);
        this.subwayPath = subwayPath;
    }

    public int getDistance() {
        return this.subwayPath.calculateDistance();
    }

    public Fare getFare() {
        return this.fare;
    }
}
