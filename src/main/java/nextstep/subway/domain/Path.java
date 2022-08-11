package nextstep.subway.domain;

import nextstep.subway.domain.fare.FareStrategy;

import java.util.List;

public class Path {
    private Sections sections;
    private int shortestDistance;
    private int age;

    public Path(Sections sections) {
        this.sections = sections;
        this.shortestDistance = sections.totalDistance();
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
        return sections.getTotalDuration();
    }

    public void setShortestDistance(int shortestDistance) {
        this.shortestDistance = shortestDistance;
    }

    public int extractFare() {
        return calculateFare(shortestDistance);
    }

    private int calculateFare(int distance) {
        FareStrategy fareStrategy = FareType.findStrategy(distance);
        return fareStrategy.calculate(distance);
    }

    public int getMaxExtraFare() {
        return sections.getMaxExtraFare();
    }
}
