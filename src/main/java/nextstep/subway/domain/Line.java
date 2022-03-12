package nextstep.subway.domain;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.function.Supplier;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    @ColumnDefault("0")
    private int extraCharge;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, int extraCharge) {
        this.name = name;
        this.color = color;
        this.extraCharge = extraCharge;
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

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void update(String name, String color, int extraCharge) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
        this.extraCharge = extraCharge;
    }

    public void addSection(Supplier<Section> supplier) {
        sections.add(supplier.get());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int getExtraCharge() {
        return extraCharge;
    }

    public void deleteSection(Station station) {
        sections.delete(station);
    }
}
