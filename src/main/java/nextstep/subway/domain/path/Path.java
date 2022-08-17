package nextstep.subway.domain.path;

import java.util.List;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.finder.FareCalculator;

public class Path {
    private final Sections sections;
    private final int shortDistance;
    private final int age;

    public Path(Sections sections, int shortDistance, int age) {
        this.sections = sections;
        this.shortDistance = shortDistance;
        this.age = age;
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

    public int extractFare() {
        int fare = new FareCalculator().calculator(shortDistance);
        fare += sections.getMaxLineFare();

        return byAgeCaculateFare(fare);
    }

    private int byAgeCaculateFare(int fare) {
        if (age >= 6 && age < 13) {
            return (fare - 350) / 2;
        } else if (age >= 13 && age < 19) {
            return (int) ((fare - 350) * 0.8);
        }

        return fare;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
