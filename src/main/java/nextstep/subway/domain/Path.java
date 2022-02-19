package nextstep.subway.domain;

import java.time.LocalDateTime;
import java.util.List;

import nextstep.subway.domain.map.Groups;
import nextstep.subway.domain.map.SubwayDispatchTime;

public class Path {
    private final Sections sections;
    private LocalDateTime arrivalTime;

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

    public void applyArrivalTime(LocalDateTime takeTime) {
        this.arrivalTime = findArrivalTime(takeTime);
    }

    private LocalDateTime findArrivalTime(LocalDateTime takeTime) {
        Groups<Line, Section> groups = new Groups<>();
        for (Section groupSection : sections) {
            groups.put(groupSection.getLine(), groupSection);
        }

        LocalDateTime ongoingTime = takeTime;
        for (List<Section> eachGroup : groups) {
            SubwayDispatchTime dispatchTime = dispatchTime(eachGroup.get(0));
            List<Integer> durations = new Sections(eachGroup).durations();

            ongoingTime = dispatchTime.findArrivalTime(ongoingTime, durations);
        }
        return ongoingTime;
    }

    private SubwayDispatchTime dispatchTime(Section section) {
        return section.getLine()
                      .getDispatchTime(section.getUpStation());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
}
