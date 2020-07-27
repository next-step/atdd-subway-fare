package nextstep.subway.maps.map.application;

import nextstep.subway.maps.map.domain.SubwayPath;

public class ProportionalDistance {
    public int calculateFare(SubwayPath subwayPath, SubwayPath shortestDistancePath) {
        int distance = subwayPath.calculateDistance();

        if (shortestDistancePath == null) {
            return subwayPath.calculateFare(distance);
        }

        int shortestDistance = shortestDistancePath.calculateDistance();
        return subwayPath.calculateFare(shortestDistance);
    }
}
