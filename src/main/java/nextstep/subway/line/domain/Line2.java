package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Line2 {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String color;

  @Embedded private final LineSections2 lineSections = new LineSections2();

  @Builder
  public Line2(Long id, String name, String color, LineSections2 lineSections) {
    this.id = id;
    this.name = name;
    this.color = color;
    this.lineSections.addAll(lineSections);
  }

  public Line2(String name, String color, LineSection2... lineSections) {
    this(null, name, color, new LineSections2(Arrays.asList(lineSections)));
  }

  public Line2(String name, String color) {
    this(null, name, color, new LineSections2());
  }

  public void changeName(String name) {
    this.name = name;
  }

  public void changeColor(String color) {
    this.color = color;
  }

  public void addLineSection(LineSection2 lineSection) {
    lineSections.add(lineSection);
  }

  public List<Station> getStations() {
    return lineSections.getStations();
  }

  public void remove(Station station) {
    lineSections.remove(station);
  }
}
