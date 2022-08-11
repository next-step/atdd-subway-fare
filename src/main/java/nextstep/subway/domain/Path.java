package nextstep.subway.domain;

import java.util.List;

public class Path {

    private static final int BASIC_FARE = 1_250;

    private static final int BASIC_FARE_KM = 10;

    private static final int _100WON_PER_5KM_BASED_KM = 50;

    private static final int EXTRA_FARE_BASIC_UNIT = 100;

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

    public int extractFare() {
        return BASIC_FARE + calculateOverFare(extractDistance());
    }

    private int calculateOverFare(int distance) {
        if (distance <= BASIC_FARE_KM) {
            return 0;
        }

        if (distance <= _100WON_PER_5KM_BASED_KM) {
            return calculateOverFare(BASIC_FARE_KM, distance, 5);
        }

        int extraFareUnder50Km = calculateOverFare(BASIC_FARE_KM, _100WON_PER_5KM_BASED_KM, 5);
        int extraFareOver50Km = calculateOverFare(_100WON_PER_5KM_BASED_KM, distance, 8);

        return extraFareUnder50Km + extraFareOver50Km;

    }

    private int calculateOverFare(int from, int to, int divisor) {
        return (int) ((Math.ceil((to - from - 1) / divisor) + 1) * EXTRA_FARE_BASIC_UNIT);
    }
}
