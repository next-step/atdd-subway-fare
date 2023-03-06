package nextstep.subway.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Integer additionalFare;
    private LocalTime firstTime;
    private LocalTime lastTime;
    private int intervalMinute;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color) {
        this(name, color, 0);
    }

    public Line(String name, String color, Integer additionalFare) {
        this(name, color, additionalFare, LocalTime.of(0, 0), LocalTime.of(0, 0), 0);
    }

    public Line(String name, String color, Integer additionalFare, LocalTime firstTime, LocalTime lastTime, int intervalMinute) {
        if (firstTime.isAfter(lastTime)) {
            throw new IllegalArgumentException("첫차 시간이 막차 시간보다 늦을 수 없습니다.");
        }
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        this.firstTime = firstTime;
        this.lastTime = lastTime;
        this.intervalMinute = intervalMinute;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getAdditionalFare() {
        return additionalFare;
    }

    public LocalTime getFirstTime() {
        return firstTime;
    }

    public LocalTime getLastTime() {
        return lastTime;
    }

    public int getIntervalMinute() {
        return intervalMinute;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void update(String name, String color, Integer additionalFare) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
        if (additionalFare != null) {
            this.additionalFare = additionalFare;
        }
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        addSection(upStation, downStation, distance, 0);
    }

    public void addSection(Station upStation, Station downStation, int distance, int duration) {
        sections.add(new Section(this, upStation, downStation, distance, duration));
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void deleteSection(Station station) {
        sections.delete(station);
    }

    public LocalDateTime getSectionSchedule(Section findSection, LocalDateTime date) {
        LocalDateTime schedule = getFirstUpNextSchedule(date);
        List<Section> sections = this.sections.getSortedSections();
        for (Section section : sections) {
            if (section.equals(findSection)) {
                break;
            }
            schedule = schedule.plusMinutes(section.getDuration());
        }
        return schedule;
    }


    public LocalDateTime getFirstUpNextSchedule(LocalDateTime date) {
        List<LocalTime> schedule = getFirstUpSchedule();
        if (schedule.isEmpty()) {
            throw new IllegalArgumentException("노선 스케줄이 비어있습니다.");
        }
        LocalTime firstTime = schedule.get(0);
        return schedule.stream()
                .sorted()
                .filter(time -> time.equals(date.toLocalTime()) || time.isAfter(date.toLocalTime()))
                .findFirst()
                .map(time -> LocalDateTime.of(date.toLocalDate(), time))
                .orElse(LocalDateTime.of(date.toLocalDate().plusDays(1), firstTime));
    }

    public List<LocalTime> getFirstUpSchedule() {
        List<LocalTime> schedule = new ArrayList<>();
        LocalTime currentTime = firstTime;
        while (currentTime.isBefore(lastTime)) {
            schedule.add(currentTime);
            currentTime = currentTime.plusMinutes(intervalMinute);
        }
        schedule.add(lastTime);
        return schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(getName(), line.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
