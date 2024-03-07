package nextstep.path;

import lombok.extern.slf4j.Slf4j;
import nextstep.line.Line;
import nextstep.line.section.Section;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class ArrivalTimeCalculator {
    private List<Section> path;

    private LocalTime departureTime;

    public ArrivalTimeCalculator(List<Section> path, LocalTime departureTime) {
        this.path = path;
        this.departureTime = departureTime;
    }

    public LocalTime calculate() {
        LocalTime currentTime = departureTime;

        for (Section section : path) {
            Line line = section.getLine();
            int intervalMinutes = line.getIntervalTime();
            LocalTime firstDeparture = line.getFirstDepartureTime();
            LocalTime lastDeparture = line.getLastDepartureTime();

            int minutesSinceFirstDeparture = (int) ChronoUnit.MINUTES.between(firstDeparture, currentTime) % intervalMinutes;
            int waitMinutes = intervalMinutes - minutesSinceFirstDeparture;
            LocalTime nextTrainTime = currentTime.plusMinutes(waitMinutes);

            if (nextTrainTime.isAfter(lastDeparture)) {
                nextTrainTime = firstDeparture;
            }

            currentTime = nextTrainTime.plusMinutes(section.getDuration());
        }

        return currentTime;
    }
}
