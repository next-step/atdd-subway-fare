package nextstep.subway.domain.fareOption;

import java.util.Set;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;

public class FareCalculateDistanceOption implements FareCalculateOption {
    private int distanceOver;
    private int distanceUnder;
    private int chargingUnitDistance;
    private int fare;

    public FareCalculateDistanceOption() {
    }

    public FareCalculateDistanceOption(int distanceOver, int distanceUnder, int chargingUnitDistance, int fare) {
        this.distanceOver = distanceOver;
        this.distanceUnder = distanceUnder;
        this.chargingUnitDistance = chargingUnitDistance;
        this.fare = fare;
    }

    public int getChargingUnitDistance() {
        return chargingUnitDistance;
    }

    public int getFare() {
        return fare;
    }

    @Override
    public boolean isCalculateTarget(int distance, Set<LineResponse> line) {
        return distance > distanceOver;
    }

    public int getOverDistance(int distance) {
        if (isUpperboundValue(distance)) {
            return distanceUnder - distanceOver;
        }

        return distance - distanceOver;
    }

    public FareCalculateDistanceOption setDistanceOver(int distanceOver) {
        this.distanceOver = distanceOver;
        return this;
    }

    public FareCalculateDistanceOption setDistanceUnder(int distanceUnder) {
        this.distanceUnder = distanceUnder;
        return this;
    }

    public FareCalculateDistanceOption setChargingUnitDistance(int chargingUnitDistance) {
        this.chargingUnitDistance = chargingUnitDistance;
        return this;
    }

    public FareCalculateDistanceOption setFare(int fare) {
        this.fare = fare;
        return this;
    }

    public FareCalculateDistanceOption build() {
        return this;
    }

    @Override
    public int calculateFare(int distance, Set<LineResponse> line) {
        int overDistance = getOverDistance(distance);
        if (overDistance < 1) {
            return 0;
        }

        return (int) Math.ceil((double) overDistance / chargingUnitDistance) * fare;
    }

    private boolean isUpperboundValue(int distance) {
        return distance > distanceUnder;
    }


}
