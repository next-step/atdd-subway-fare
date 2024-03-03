package nextstep.line.section;

import lombok.*;
import nextstep.exception.InvalidInputException;
import nextstep.line.Line;
import nextstep.station.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Setter
    private Line line;

    @ManyToOne
    @Setter
    private Station upstation;

    @ManyToOne
    @Setter
    private Station downstation;

    @Setter
    private int distance;

    @Setter
    private int duration;

    public static Section initSection(Line line, Station upstation, Station downstation, int distance, int duration) {
        return Section.builder()
                .line(line)
                .upstation(upstation)
                .downstation(downstation)
                .distance(distance)
                .duration(duration)
                .build();
    }

    public boolean isInSection(Section section) {
        return downstation.equals(section.getDownstation()) ||
                upstation.equals(section.getUpstation());
    }

    public boolean isUpstation(Station station) {
        return upstation.equals(station);
    }

    public boolean isDownstation(Station station) {
        return downstation.equals(station);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;
        Section section = (Section) o;
        if (id != null && section.id != null) {
            return Objects.equals(id, section.id);
        } else {
            return Objects.equals(upstation, section.upstation) &&
                    Objects.equals(downstation, section.downstation);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id != null ? id : upstation, downstation);
    }

    public void shorten(Section newSection) {
        int newDistance = distance - newSection.getDistance();
        if (newDistance <= 0) {
            throw new InvalidInputException("유효하지 않은 거리입니다.");
        }

        int newDuration = duration - newSection.getDuration();
        if (newDuration <= 0) {
            throw new InvalidInputException("유효하지 않은 소요시간입니다.");
        }

        distance = newDistance;
        duration = newDuration;
    }

    public void extend(Section removedSection) {
        distance = distance + removedSection.getDistance();
        duration = duration + removedSection.getDuration();
    }
}
