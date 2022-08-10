package nextstep.subway.domain;

import java.util.List;

public class Path {
    public static final int BASIC_FARE = 1250;
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
        int price = BASIC_FARE;
        int distance = extractDistance();

        return calculatePrice(price, distance);
    }

    private int calculatePrice(int price, int distance) {
        if (distance <= 10) {
            return price;
        }

        if (distance <= 50) {
            return price + calculateExcessFareOver10(distance);
        }

        return price + calculateExcessFareOver10(50) + calculateExcessFareOver50(distance);
    }

    private int calculateExcessFareOver10(int distance) {
        int overDistance = distance - 10;

        return (int) ((Math.ceil((overDistance - 1) / 5) + 1) * 100);
    }

    private int calculateExcessFareOver50(int distance) {
        int overDistance = distance - 50;
        return (int) ((Math.ceil((overDistance - 1) / 8) + 1) * 100);
    }
}
