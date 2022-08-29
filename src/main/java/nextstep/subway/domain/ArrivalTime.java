package nextstep.subway.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static nextstep.support.entity.Formatters.DATE_TIME_PATH;

public class ArrivalTime {
    private Sections sections;
    private Lines lines;
    private LocalDateTime dateTime;
    private LocalTime startTime;

    public ArrivalTime(List<Section> sections, List<Line> lines, String time) {
        this.sections = new Sections(sections);
        this.lines = new Lines(lines);
        this.dateTime = LocalDateTime.parse(time, DATE_TIME_PATH);
        this.startTime = findLine(sections.get(0)).getStartTime();
    }

    public LocalDateTime value() {
        return getTimeOfFastestPath(dateTime);
    }

    private LocalDateTime getTimeOfFastestPath(LocalDateTime dateTime) {
        LocalDateTime currentDateTime = dateTime;
        Line currentLine = null;

        for (Section section : sections.getSections()) {
            Line line = findLine(section);
            if (!line.equals(currentLine)) {
                currentLine = line;
                currentDateTime = calculateTimeInBeforeStations(currentDateTime, currentLine, section);

                if (goingLastTime(currentDateTime, currentLine.getEndTime())) {
                    return getTimeOfFirstPath();
                }
            }

            currentDateTime = currentDateTime.plusMinutes(section.getDuration());
        }

        return currentDateTime;
    }

    private LocalDateTime getTimeOfFirstPath() {
        LocalDateTime nextDayAtStartDateTime = changeTime(dateTime.plusDays(1), startTime);
        return getTimeOfFastestPath(nextDayAtStartDateTime);
    }

    private boolean goingLastTime(LocalDateTime dateTime, LocalTime endTime) {
        return dateTime.isAfter(changeTime(dateTime, endTime));
    }

    private LocalDateTime calculateTimeInBeforeStations(LocalDateTime currentDateTime, Line currentLine, Section section) {
        LocalDateTime targetTime = changeTime(currentDateTime, currentLine.getStartTime());
        LocalDateTime fastestDepartureTime = findFastestDepartureTime(currentDateTime, targetTime, currentLine.getIntervalTime());
        int beforeDuration = getBeforeDuration(currentLine, section);
        return fastestDepartureTime.plusMinutes(beforeDuration);
    }

    private Line findLine(Section section) {
        return lines.indexOf(section);
    }

    private LocalDateTime changeTime(LocalDateTime dateTime, LocalTime time) {
        return LocalDateTime.of(dateTime.toLocalDate(), time);
    }

    private int getBeforeDuration(Line line, Section currentSection) {
        List<Section> beforeSections = new ArrayList<>();
        for (Section section : line.getSections()) {
            if (section.equals(currentSection)) {
                break;
            }
            beforeSections.add(section);
        }

        return new Sections(beforeSections).totalDuration();
    }

    private LocalDateTime findFastestDepartureTime(LocalDateTime referenceTime, LocalDateTime targetTime, int interval) {
        if (referenceTime.isAfter(targetTime)) {
            return findFastestDepartureTime(referenceTime, targetTime.plusMinutes(interval), interval);
        }
        return targetTime;
    }

    private LocalTime findTimeRequired(LocalTime baseTime, LocalTime time, int interval) {
        if (baseTime.isAfter(time)) {
            return findTimeRequired(baseTime, time.plusMinutes(interval), interval);
        }
        return time;
    }
}
