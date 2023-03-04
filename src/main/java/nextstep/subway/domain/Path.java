package nextstep.subway.domain;

import java.util.List;

import static nextstep.subway.domain.Fare.DEFAULT_FARE;

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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public int calculateFare(int age) {
        int totalDistance = extractDistance();
        List<Line> lines = sections.getLines();

        Fare fare = DEFAULT_FARE
                .plus(OverDistanceFarePolicy.create().calculateOverDistanceFare(totalDistance))
                .plus(new AdditionalLineFarePolicy().calculateAdditionalLineFare(lines));

        return fare.minus(AgeDiscountPolicy.calculateAgeDiscountFare(fare, age)).getValue();
    }
}
