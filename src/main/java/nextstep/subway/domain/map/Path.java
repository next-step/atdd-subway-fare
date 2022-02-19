package nextstep.subway.domain.map;

import java.time.LocalDateTime;
import java.util.List;

import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

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
        LocalDateTime ongoingTime = takeTime;
        List<TransferLine> lineTransferMediators = TransferLineBuilder.create(sections);
        for (TransferLine eachTransferLine : lineTransferMediators) {
            SubwayDispatchTime dispatchTime = eachTransferLine.firstDispatchTime();
            ongoingTime = dispatchTime.findArrivalTime(
                ongoingTime, eachTransferLine.durations()
            );
        }
        return ongoingTime;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
}
