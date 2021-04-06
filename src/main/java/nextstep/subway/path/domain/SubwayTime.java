package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class SubwayTime {

    private final Line line;

    public SubwayTime(Line line) {
        this.line = line;
    }

    public LocalDateTime getNextStartTime(Station source, LocalDateTime dateTime) {
        LocalTime startTime = line.getStartTime();
        LocalTime endTime = line.getEndTime();
        LocalTime findTime = LocalTime.of(dateTime.getHour(), dateTime.getMinute());

        int addTime = line.getSections().getAddTimeFromUpStation(source);

        while (startTime.isBefore(endTime) || startTime.equals(endTime)) {
            LocalTime calculatedTime = startTime.plusMinutes(addTime);
            if (calculatedTime.equals(findTime) || calculatedTime.isAfter(findTime)) {
                return LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(),
                        calculatedTime.getHour(), calculatedTime.getMinute());
            }

            startTime = startTime.plusMinutes(line.getIntervalTime());
        }

        LocalDateTime tomorrow = dateTime.plusDays(1);
        return LocalDateTime.of(tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDayOfMonth(),
                line.getStartTime().getHour(), line.getStartTime().getMinute());
    }

    public LocalDateTime getArriveTime(Station source, Station target, LocalDateTime dateTime) {
        LocalDateTime nextStartTime = getNextStartTime(source, dateTime);
        int addTime = line.getSections().getAddTimeBetweenSourceAndTarget(source, target);
        return nextStartTime.plusMinutes(addTime);
    }
}
