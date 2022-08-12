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
        FareStrategy fareStrategy = FareType.findStrategy(shortestDistance);
        int extraFare = extractMaxExtraFare();
        return fareStrategy.calculateWithAge(shortestDistance, age, extraFare);
    }

    public int extractMaxExtraFare() {
        return sections.getMaxExtraFare();
    }

    public void setAge(int age) {
        this.age = age;
    }
}
