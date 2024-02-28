package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.member.domain.AgeRange;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import java.util.EnumSet;
import java.util.List;

public class Path {
    public static final long DEFAULT_FARE = 1250L;
    private final List<Station> stations;
    private final Long distance;
    private final Long duration;

    public Path(List<Station> stations,
                Long distance,
                Long duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public Path(List<Station> shortestPath,
                Double shortestValue,
                Long value,
                PathType type) {
        this(shortestPath, getDistance(shortestValue, value, type), getDuration(shortestValue, value, type));
    }

    private static Long getDistance(Double shortestValue,
                                    Long value,
                                    PathType type) {
        if (type == PathType.DISTANCE) {
            return Math.round(shortestValue);
        }
        return value;
    }

    private static Long getDuration(Double shortestValue,
                                    Long value,
                                    PathType type) {
        if (type == PathType.DURATION) {
            return Math.round(shortestValue);
        }
        return value;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getDuration() {
        return this.duration;
    }

    public Long fare(Lines lines,
                     Member member) {
        return calculateMemberFare(fare(lines), member);
    }

    private Long calculateMemberFare(Long fare,
                                     Member member) {
        AgeRange ageRange = member.range();
        AgeRangeFare ageRangeFare = AgeRangeFare.from(ageRange);
        return ageRangeFare.calculateFare(fare);
    }

    public Long fare(Lines lines) {
        return calculateFare() + calculateSurcharge(lines);
    }

    private Long calculateFare() {
        return DEFAULT_FARE + EnumSet.allOf(DistanceFare.class).stream()
                .filter(distanceFare -> this.distance > distanceFare.getStartDistance())
                .mapToInt(distanceFare -> calculateFare(this.distance, distanceFare))
                .sum();
    }

    private int calculateFare(Long distance,
                              DistanceFare distanceFare) {
        long applicableDistance = Math.min(distance, distanceFare.getEndDistance()) - distanceFare.getStartDistance();
        return (int) ((Math.ceil((double) applicableDistance / distanceFare.getDistanceUnit())) * distanceFare.getUnitFare());
    }

    private Long calculateSurcharge(Lines lines) {
        List<Long> surcharges = lines.findSurcharges(this.stations);
        return surcharges.stream()
                .max(Long::compareTo)
                .orElse(0L);
    }

}
