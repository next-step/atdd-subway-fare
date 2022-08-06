package nextstep.subway.domain;

import java.util.List;

public class Path {
    public static final int DEFAULT_FARE = 1250;
    public static final int TEN_KILO_METER = 10;
    public static final int FIFTY_KILO_METER = 50;
    public static final int EVERY_5KM = 5;
    public static final int EVERY_8KM = 8;
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

    public int getFare() {
        int distance = sections.totalDistance();
        if (distance < TEN_KILO_METER) {
            return DEFAULT_FARE;
        }
        if (distance < FIFTY_KILO_METER) {
            return DEFAULT_FARE + calculateOverFare(distance - TEN_KILO_METER, EVERY_5KM);
        }
        return DEFAULT_FARE + calculateOverFare(FIFTY_KILO_METER - TEN_KILO_METER, EVERY_5KM)
                + calculateOverFare(distance - FIFTY_KILO_METER, EVERY_8KM);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    private int calculateOverFare(int distance, int overKiloMeter) {
        return (int) ((Math.ceil((distance - 1) / overKiloMeter) + 1) * 100);
    }
}
