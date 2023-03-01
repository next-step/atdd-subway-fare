package nextstep.subway.domain;

import java.util.List;

import static nextstep.subway.domain.Fare.BASIC_FARE;
import static nextstep.subway.domain.Fare.OVER_FARE;
import static nextstep.subway.domain.OverFareLevel.LEVEL1;
import static nextstep.subway.domain.OverFareLevel.LEVEL2;

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
        if (distance <= LEVEL1.getLimit()) {
            return BASIC_FARE.getAmount();
        }
        if (distance <= LEVEL2.getLimit()) {
            return BASIC_FARE.getAmount()
                + calculateOverFare(distance - LEVEL1.getLimit(), LEVEL1.getInterval());
        }
        return BASIC_FARE.getAmount()
            + calculateOverFare(LEVEL2.getLimit() - LEVEL1.getLimit(), LEVEL1.getInterval())
            + calculateOverFare(distance - LEVEL2.getLimit(), LEVEL2.getInterval());
    }

    private int calculateOverFare(int distance, int interval) {
        return (int) ((Math.ceil((distance - 1) / interval) + 1) * OVER_FARE.getAmount());
    }
}
