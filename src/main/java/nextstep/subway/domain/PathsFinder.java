package nextstep.subway.domain;

import static nextstep.subway.utils.StringDateTimeConverter.convertLineTimeToDateTime;

import java.time.LocalDateTime;
import java.util.List;

public class PathsFinder {
    private final LocalDateTime startTime;

    private LocalDateTime fastestArrivalTime = LocalDateTime.MAX;
    private int fareDistance = Integer.MAX_VALUE;

    private List<Section> shortestSectionList = null;

    public PathsFinder(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getFastestArrivalTime() {
        return fastestArrivalTime;
    }

    public LocalDateTime getShortestDistanceArrivalTime() {
        return findArrivalTime(shortestSectionList, startTime);
    }

    public List<Section> getShortestSectionList() {
        return shortestSectionList;
    }

    public void processUsingArrivalTime(List<Section> sectionList) {
        LocalDateTime currentArrivalTime = findArrivalTime(sectionList, startTime);

        if (currentArrivalTime.isBefore(fastestArrivalTime)) {
            shortestSectionList = sectionList;
            fastestArrivalTime = currentArrivalTime;
        }
    }

    public void processUsingDistance(List<Section> sectionList) {
        int distance = totalDistanceOf(sectionList);

        if (distance <= fareDistance) {
            fareDistance = distance;
            shortestSectionList = sectionList;
        }
    }

    protected LocalDateTime findArrivalTime(List<Section> sectionList, LocalDateTime startTime) {
        LocalDateTime currentTime = startTime;

        Section prevSection = null;
        for (Section section : sectionList) {
            currentTime = processingArrivalTime(section, prevSection, currentTime);
            prevSection = section;
        }

        return currentTime;
    }

    private LocalDateTime processingArrivalTime(Section section, Section prevSection, LocalDateTime currentTime) {
        if (prevSection == null || !prevSection.getLine().equals(section.getLine())) {
            LocalDateTime trainTime = findTrainTime(section, currentTime);
            currentTime = trainTime;
        }

        return currentTime.plusMinutes(section.getDuration());
    }

    protected LocalDateTime findTrainTime(Section section, LocalDateTime currentTime) {
        Station targetStation = section.getUpStation();
        Line line = section.getLine();

        PathDirection pathDirection = findPathDirection(section);
        int durationTimeToStation = findDurationTimeToStation(line, targetStation, pathDirection);
        LocalDateTime trainTime = findFirstArrivedTrainTime(currentTime, line, durationTimeToStation);

        return trainTime.plusMinutes(durationTimeToStation);
    }

    private LocalDateTime findFirstArrivedTrainTime(LocalDateTime currentTime, Line line, int durationTimeToStation) {
        LocalDateTime startTime = convertLineTimeToDateTime(currentTime, line.getStartTime());
        int intervalTime = line.getIntervalTime();

        LocalDateTime trainTime = startTime;
        while (!currentTime.isBefore(trainTime.plusMinutes(durationTimeToStation))
            && !currentTime.isEqual(trainTime.plusMinutes(durationTimeToStation))) {
            trainTime = trainTime.plusMinutes(intervalTime);
        }

        return trainTime;
    }

    private int findDurationTimeToStation(Line line, Station targetStation, PathDirection pathDirection) {
        List<Section> sectionList = line.getSections();

        if (pathDirection.equals(PathDirection.UP)) {
            return calculateFromTimeDownToUp(sectionList, targetStation);
        }

        return calculateTimeFromUpToDown(sectionList, targetStation);
    }

    private int calculateFromTimeDownToUp(List<Section> sectionList, Station targetStation) {
        DurationMinutes durationMinutes = new DurationMinutes();

        for (int i = sectionList.size()-1; i >= 0; --i) {
            Section section = sectionList.get(i);

            durationMinutes.processDownToUp(section, targetStation);
        }

        return durationMinutes.getSum();
    }



    private int calculateTimeFromUpToDown(List<Section> sectionList, Station targetStation) {
        DurationMinutes durationMinutes = new DurationMinutes();

        for (Section section : sectionList) {
            durationMinutes.processUpToDown(section, targetStation);
        }

        return durationMinutes.getSum();
    }

    protected PathDirection findPathDirection(Section section) {
        Line line = section.getLine();
        List<Station> stations = line.getStations();

        Station currentStation = section.getUpStation();
        Station nextStation = section.getDownStation();

        int i = 0;
        if (stations.get(i).equals(currentStation)) {
            return PathDirection.DOWN;
        }

        while (++i < stations.size()) {
            if (currentStation.equals(stations.get(i))) {
                if (nextStation.equals(stations.get(i-1))) {
                    return PathDirection.UP;
                }
                break;
            }
        }

        return PathDirection.DOWN;
    }

    private int totalDistanceOf(List<Section> sectionList) {
        return sectionList.stream()
            .mapToInt(Section::getDistance)
            .sum();
    }

    public int totalDistance() {
        return totalDistanceOf(shortestSectionList);
    }
}
