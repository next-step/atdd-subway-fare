package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.common.exception.SectionNotFoundException;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Path {
    private static final int BASE_FARE = 1250;
    private static final int CHILD_AGE_MIN = 6;
    private static final int CHILD_AGE_MAX = 13;
    private static final int TEENAGER_AGE_MIN = 13;
    private static final int TEENAGER_AGE_MAX = 19;
    private static final int FARE_DEDUCTION = 350;
    private static final double CHILD_DISCOUNT_RATE = 0.5;
    private static final double TEENAGER_DISCOUNT_RATE = 0.8;
    private static final int SHORT_DISTANCE_LIMIT = 10;
    private static final int LONG_DISTANCE_LIMIT = 50;
    private static final int DISTANCE_FARE_UNIT = 5;
    private static final int DISTANCE_FARE_AMOUNT = 100;
    private static final int LONG_DISTANCE_BASE_FARE = 800;
    private static final int LONG_DISTANCE_FARE_UNIT = 8;

    private List<Section> sections = new ArrayList<>();
    private final List<Station> stations;
    private final PathType pathType;
    private final int totalWeight;
    private int fare;
    private int distance;
    private int duration;

    private Path(List<Station> stations, List<Section> allSections, PathType pathType, int totalWeight, int age) {
        this.stations = stations;
        this.pathType = pathType;
        this.totalWeight = totalWeight;
        calculateTotalAttribute(stations, allSections);
        this.fare = calculateFare(age);
    }

    public static Path createPath(List<Station> stations, List<Section> allSections, PathType pathType, int totalWeight, int age) {
        return new Path(stations, allSections, pathType, totalWeight, age);
    }

    private void calculateTotalAttribute(List<Station> pathStations, List<Section> allSections) {
        for (int i = 0; i < pathStations.size() - 1; i++) {
            Station upStation = pathStations.get(i);
            Station downStation = pathStations.get(i + 1);
            Section section = findSection(upStation, downStation, allSections);

            this.sections.add(section);
            this.distance += findSectionDistance(section);
            this.duration += findSectionDuration(section);
        }
    }

    private Section findSection(Station upStation, Station downStation, List<Section> allSections) {
        return allSections.stream()
                .filter(section -> section.containsStations(upStation, downStation))
                .findFirst()
                .orElseThrow(() -> new SectionNotFoundException(upStation, downStation));
    }

    private int findSectionDistance(Section section) {
        return section.getSectionDistance().getDistance();
    }

    private int findSectionDuration(Section section) {
        return section.getSectionDuration();
    }

    private int calculateFare(int age) {
        int extraFare = calculateDistanceFare();
        int lineExtraFare = calculateLineExtraFare();
        int totalFare = BASE_FARE + extraFare + lineExtraFare;

        if (age >= CHILD_AGE_MIN && age < CHILD_AGE_MAX) {
            return Math.max(0, (int) ((totalFare - FARE_DEDUCTION) * CHILD_DISCOUNT_RATE));
        } else if (age >= TEENAGER_AGE_MIN && age < TEENAGER_AGE_MAX) {
            return Math.max(0, (int) ((totalFare - FARE_DEDUCTION) * TEENAGER_DISCOUNT_RATE));
        }

        return totalFare;
    }

    private int calculateDistanceFare() {
        if (distance <= SHORT_DISTANCE_LIMIT) {
            return 0;
        }
        if (distance <= LONG_DISTANCE_LIMIT) {
            return ((distance - SHORT_DISTANCE_LIMIT + DISTANCE_FARE_UNIT - 1) / DISTANCE_FARE_UNIT) * DISTANCE_FARE_AMOUNT;
        }
        return LONG_DISTANCE_BASE_FARE + ((distance - LONG_DISTANCE_LIMIT + LONG_DISTANCE_FARE_UNIT - 1) / LONG_DISTANCE_FARE_UNIT) * DISTANCE_FARE_AMOUNT;
    }

    private int calculateLineExtraFare() {
        return sections.stream()
                .map(Section::getLine)
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }

    public boolean isValid() {
        return !this.getStations().isEmpty() && this.getTotalWeight() > 0;
    }
}
