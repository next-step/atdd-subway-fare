package nextstep.subway.domain.line;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private int addFare;

    @Embedded
    private Sections sections = new Sections();

    public Line(String name, String color, int addFare) {
        this.name = name;
        this.color = color;
        this.addFare = addFare;
    }

    public List<Section> getSections() {
        return sections.sections();
    }

    public void update(String name, String color) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
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
