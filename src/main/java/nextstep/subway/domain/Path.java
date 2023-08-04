package nextstep.subway.domain;

import java.util.List;

public class Path {

    private static final int FIXED_AMOUNT = 1250;
    private static final int BASE_DISTANCE = 10;
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

    public int getFee() {
        return FIXED_AMOUNT + calculateOverFee(sections.totalDistance() - BASE_DISTANCE);
    }

    private int calculateOverFee(int distance) {
        if (distance <= 0) {
            return 0;
        }
        if (distance > 40) {
            return calculateOverFeeAbove40(distance);
        }
        return calculateOverFeeBelow40(distance);
    }

    private int calculateOverFeeAbove40(int distance) {
        int over40Distance = distance - 41;
        return 800 + (int) ((Math.ceil(over40Distance / 8) + 1) * 100);
    }

    private int calculateOverFeeBelow40(int distance) {
        int below40Distance = distance - 1;
        return (int) ((Math.ceil(below40Distance / 5) + 1) * 100);
    }
}
