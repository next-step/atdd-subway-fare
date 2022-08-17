package nextstep.subway.domain.path;

import java.util.List;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.finder.FareCalculator;

public class Path {

    private static final int DEDUCTION = 350;

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

        return byAgeCalculateFare(fare);
    }

    private int byAgeCalculateFare(int fare) {
        if (isChildCheck(age)) {
            return (fare - DEDUCTION) / 2;
        }

        if (isTeenCheck(age)) {
            return (int) ((fare - DEDUCTION) * 0.8);
        }

        return fare;
    }

    private boolean isChildCheck(int age) {
        return age >= 6 && age < 13;
    }

    private boolean isTeenCheck(int age) {
        return age >= 13 && age < 19;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
