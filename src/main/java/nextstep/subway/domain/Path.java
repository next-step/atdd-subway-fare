package nextstep.subway.domain;

import java.math.BigDecimal;
import java.util.List;

public class Path {
    private PathType pathType;
    private Sections sections;
    private static final String WON = "원";
    private static final int MIN_DISTANCE = 10;
    private static final int MAX_DISTANCE = 50;
    private static final int MIN_EXCEED_OVER_DISTANCE = 5;
    private static final int MAX_EXCEED_OVER_DISTANCE = 8;
    private static final int MIN_TOTAL_FARE = 1250;
    private static final int OVER_FARE = 100;

    public Path(PathType pathType, Sections sections) {
        this.pathType = pathType;
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public PathType getPathType() {
        return pathType;
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

    public String extractTotalFare() {
        BigDecimal totalFare = new BigDecimal(MIN_TOTAL_FARE);

        if (isWithInMinDistance()) {
            return  totalFare + WON;
        }

        BigDecimal overFare = calculateOverFare(sections.totalDistance()-MIN_DISTANCE);
        return  totalFare.add(overFare) + WON;
    }

    private boolean isWithInMinDistance() {
        return MIN_DISTANCE >= sections.totalDistance();
    }

    private boolean isWithInMinAndMaxDistance() {
        return MIN_DISTANCE < sections.totalDistance() && MAX_DISTANCE >= sections.totalDistance();
    }

    private BigDecimal calculateOverFare(int distance) {
        int overCount = getOverCountByStandardDistance(distance);

        return new BigDecimal(OVER_FARE).multiply(BigDecimal.valueOf(overCount));
    }

    private int getOverCountByStandardDistance(int distance) {
        if (isWithInMinAndMaxDistance()) {
            return (int) (Math.ceil((distance - 1) / MIN_EXCEED_OVER_DISTANCE) + 1);
        }

        return (int) (Math.ceil((distance - 1) / MAX_EXCEED_OVER_DISTANCE) + 1);
    }
}
