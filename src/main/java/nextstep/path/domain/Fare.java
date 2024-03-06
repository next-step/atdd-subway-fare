package nextstep.path.domain;

import nextstep.path.DistanceFare;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class Fare {
    private int fare = 0;

    private final List<LineFare> lineFares;
    private final int distance;
    private int extraFare = 0;

    public Fare(List<LineFare> lineFares, int distance) {
        this.lineFares = lineFares;
        this.distance = distance;
    }


    public void calculateFare(List<DistanceFare> distanceFares) {
        for (DistanceFare distanceFare : distanceFares) {
            fare += distanceFare.calculateFare(this);
        }
        addExtraFare();
    }

    private void addExtraFare() {
        HashSet<LineFare> fares = new HashSet<>(lineFares);
        for (LineFare lineFare : fares) {
            if (lineFare.hasExtraFare()) {
                addFare(lineFare.getExtraFare());
            }
        }
    }

    private void addFare(int extraFare) {
        this.fare += extraFare;
    }


    public int getFare() {
        return fare;
    }


    public int getDistance() {
        return distance;
    }

}
