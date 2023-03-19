package nextstep.subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    @Getter(NONE)
    private final Sections sections = new Sections();

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void update(String name, String color) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
    }

    public void addSection(final Station upStation, final Station downStation, final int distance, final int duration) {
        sections.add(new Section(this, upStation, downStation, distance, duration));
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void deleteSection(final Station station) {
        sections.delete(station);
    }
}
