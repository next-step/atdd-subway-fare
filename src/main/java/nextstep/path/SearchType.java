package nextstep.path;

import nextstep.exception.InvalidInputException;
import nextstep.station.Station;

import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.Comparator;

public enum SearchType {
    DISTANCE {
        @Override
        public Path findPath(PathFinder pathFinder, Station source, Station target, LocalTime departureTime) {
            return pathFinder.findShortestPath(source, target);
        }
    },
    DURATION {
        @Override
        public Path findPath(PathFinder pathFinder, Station source, Station target, LocalTime departureTime) {
            return pathFinder.findFastestPath(source, target);
        }
    },

    ARRIVAL_TIME {
        @Override
        public Path findPath(PathFinder pathFinder, Station source, Station target, LocalTime departureTime) {
            return pathFinder.findMultipleShortestPath(source, target).stream()
                    .map(path -> new AbstractMap.SimpleEntry<>(path, new ArrivalTimeCalculator(path.getSections(), departureTime).calculate()))
                    .min(Comparator.comparing(AbstractMap.SimpleEntry::getValue))
                    .map(AbstractMap.SimpleEntry::getKey)
                    .orElse(null);
//            List<Path> paths = pathFinder.findKShortestPath(source, target);
//
//            Path fastestPath = null;
//            LocalTime minArrivalTime = null;
//            for (Path path : paths) {
//                ArrivalTimeCalculator arrivalTimeCalculator = new ArrivalTimeCalculator(path.getSections(), departureTime);
//                LocalTime arrivalTime = arrivalTimeCalculator.calculate();
//
//                if (minArrivalTime == null || arrivalTime.isBefore(minArrivalTime)) {
//                    minArrivalTime = arrivalTime;
//                    fastestPath = path;
//                }
//            }
//
//            return fastestPath;
        }
    };

    public abstract Path findPath(PathFinder pathFinder, Station source, Station target, LocalTime departureTime);

    public static SearchType from(String type) throws InvalidInputException {
        for (SearchType searchType : values()) {
            if (searchType.name().equalsIgnoreCase(type)) {
                return searchType;
            }
        }

        throw new InvalidInputException("올바른 타입을 입력해주세요.");
    }
}