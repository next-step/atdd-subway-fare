package nextstep.subway.domain;

import java.util.List;

import static nextstep.subway.domain.Fare.BASIC_FARE;
import static nextstep.subway.domain.Fare.OVER_FARE;
import static nextstep.subway.domain.OverFareLevel.OVER_10KM;
import static nextstep.subway.domain.OverFareLevel.OVER_50KM;

public class Path {

    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getFare() {
        int distance = extractDistance();
        if (distance <= OVER_10KM.getStart()) {
            return BASIC_FARE.getAmount();
        }
        if (distance <= OVER_50KM.getStart()) {
            return BASIC_FARE.getAmount()
                + calculateOverFare(distance - OVER_10KM.getStart(), OVER_10KM.getInterval());
        }
        return BASIC_FARE.getAmount()
            + calculateOverFare(OVER_50KM.getStart() - OVER_10KM.getStart(), OVER_10KM.getInterval())
            + calculateOverFare(distance - OVER_50KM.getStart(), OVER_50KM.getInterval());
    }

    private int calculateOverFare(int distance, int interval) {
        return (int) ((Math.ceil((distance - 1) / interval) + 1) * OVER_FARE.getAmount());
    }
}
