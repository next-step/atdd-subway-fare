package nextstep.subway.domain;

import java.util.List;

public class Path {
    private final Sections sections;
    public static final int DEFAULT_FARE = 1250;
    public static final int PER_KILOMETER_OVER_TEN = 5;
    public static final int PER_KILOMETER_OVER_FIFTY = 8;
    public static final int EXTRA_FARE = 100;
    public static final int LOWER_THRESHOLD_OVER_TEN = 10;
    public static final int UPPER_THRESHOLD_OVER_FIFTY = 50;
    public static final int MINIMUM = 0;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }
    public int extractDuration() {
        return sections.totalDuration();
    }
    public int extractFare() {
        return calculateFare(sections.totalDistance());
    }
    public List<Station> getStations() {
        return sections.getStations();
    }
    // TODO Money 객체 만들기랑 calculate를 객체로 빼야 될까요?
    //      계산 로직이 복잡해지면 path 가 distance 이외의 값을 받게 된다면 path 객체가 복잡성이 증가하면
    //      Money 객체랑 calculateFare를 Calculator로 따로 로직을 빼서 진행할 예정입니다.
    //
    //      Fare 계산이 Path 상태에 의존하고 있어서 Path를 불변객체로 뽑아서 상태변경을 제거했습니다.
    public int calculateFare(int distance) {
        int fare = DEFAULT_FARE;

        int distanceOverTen = extractOverDistance(distance, LOWER_THRESHOLD_OVER_TEN, UPPER_THRESHOLD_OVER_FIFTY);
        fare += calculateOverFare(distanceOverTen, PER_KILOMETER_OVER_TEN, EXTRA_FARE);

        int distanceOverFifty = extractOverDistance(distance, 50, Integer.MAX_VALUE);
        fare += calculateOverFare(distanceOverFifty, PER_KILOMETER_OVER_FIFTY, EXTRA_FARE);

        return fare;
    }
    private int extractOverDistance(int distance, int lowerThreshold, int upperThreshold){
        if (distance >= upperThreshold){
            return upperThreshold - lowerThreshold;
        }
        return Math.max(distance - lowerThreshold, 0);
    }

    private int calculateOverFare(int distance, int perKilometer, int extraFare) {
        if (distance == MINIMUM) {
            return MINIMUM;
        }
        return (int) ((Math.ceil((distance - 1) / perKilometer) + 1) * extraFare);
    }
}
