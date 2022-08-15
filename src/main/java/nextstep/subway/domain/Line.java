package nextstep.subway.domain;

import nextstep.subway.domain.Section.SectionBuilder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private int additionalFare;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color) {
        this(name, color, 0);
    }

    public Line(String name, String color, int additionalFare) {
        this(name, color, additionalFare, null, null, 0);
    }

    public Line(String name, String color, int additionalFare, LocalTime startTime, LocalTime endTime, int intervalTime) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }

    public static LineBuilder builder() {
        return new LineBuilder();
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public int getAdditionalFare() {
        return additionalFare;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void update(String name, String color, int additionalFare, LocalTime startTime, LocalTime endTime, int intervalTime) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
        this.additionalFare = additionalFare;
        if (startTime != null) {
            this.startTime = startTime;
        }
        if (endTime != null) {
            this.endTime = endTime;
        }
        this.intervalTime = intervalTime;
    }

    public void addSection(Station upStation, Station downStation, int distance, int duration) {
        sections.add(new Section(this, upStation, downStation, distance, duration));
    }

    public void addSection(SectionBuilder sectionBuilder) {
        Section build = sectionBuilder
                .line(this)
                .build();
        sections.add(build);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void deleteSection(Station station) {
        sections.delete(station);
    }

    public static class LineBuilder {
        private String name;
        private String color;
        private int additionalFare;
        private LocalTime startTime;
        private LocalTime endTime;
        private int intervalTime;

        public LineBuilder name(String name) {
            this.name = name;
            return this;
        }

        public LineBuilder color(String color) {
            this.color = color;
            return this;
        }

        public LineBuilder additionalFare(int additionalFare) {
            this.additionalFare = additionalFare;
            return this;
        }

        public LineBuilder startTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public LineBuilder endTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public LineBuilder intervalTime(int intervalTime) {
            this.intervalTime = intervalTime;
            return this;
        }

        public Line build() {
            Line line = new Line();
            if (this.name != null) {
                line.name = this.name;
            }
            if (this.color != null) {
                line.color = this.color;
            }
            if (this.startTime != null) {
                line.startTime = this.startTime;
            }
            if (this.endTime != null) {
                line.endTime = this.endTime;
            }
            line.additionalFare = this.additionalFare;
            line.intervalTime = this.intervalTime;
            return line;
        }
    }

}
