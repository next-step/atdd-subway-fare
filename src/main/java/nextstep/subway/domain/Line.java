package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;
import nextstep.common.exception.CustomException;
import nextstep.common.exception.LineErrorMessage;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Integer extraFare;

    @Embedded
    private Sections sections = new Sections();

    public Line() {}

    public Line(String name, String color) {
        this(name, color, 0);
    }

    public Line(String name, String color, int extraFare) {
        validateExtraFare(extraFare);

        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    private void validateExtraFare(int extraFare) {
        if (extraFare < 0) {
            throw new CustomException(LineErrorMessage.LINE_EXTRA_FARE_NEGATIVE);
        }
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void update(String name, String color, Integer extraFare) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }

        if (extraFare != null) {
            this.extraFare = extraFare;
        }
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
}
