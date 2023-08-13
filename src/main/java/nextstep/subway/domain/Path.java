package nextstep.subway.domain;

import java.util.List;

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

    public int calculatePrice() {
        int distance = sections.totalDistance();
        int price = 1250;
        if (distance <= 10) {
            return price;
        }
        if (distance <= 50) {
            int overFare = getOverFareFirst(distance);
            return price + overFare;
        }
        return price + getOverFareFirst(50) + getOverFareSecond(distance);
    }

    private static int getOverFareFirst(int distance) {
        return (int) Math.ceil((double) (distance - 10) / 5) * 100;
    }

    private static int getOverFareSecond(double distance) {
        return (int) Math.ceil((distance - 50) / 8) * 100;
    }

}
