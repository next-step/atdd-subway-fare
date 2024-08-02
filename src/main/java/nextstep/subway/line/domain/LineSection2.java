package nextstep.subway.line.domain;

import java.util.List;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LineSection2 {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "up_station_id")
  private Station upStation;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "down_station_id")
  private Station downStation;

  private int distance;
  private int duration;

  @Builder
  public LineSection2(Long id, Station upStation, Station downStation, int distance, int duration) {
    this.id = id;
    this.upStation = upStation;
    this.downStation = downStation;
    this.distance = distance;
    this.duration = duration;
  }

  public LineSection2(Station upStation, Station downStation, int distance, int duration) {
    this(null, upStation, downStation, distance, duration);
  }

  public static LineSection2 of(
      Station upStation, Station downStation, int distance, int duration) {
    return new LineSection2(upStation, downStation, distance, duration);
  }

  public boolean canPrepend(LineSection2 lineSection) {
    return upStation.isSame(lineSection.downStation);
  }

  public boolean canAppend(LineSection2 lineSection) {
    return downStation.isSame(lineSection.upStation);
  }

  public boolean canSplitUp(LineSection2 lineSection) {
    if (lineSection.distance >= distance) {
      return false;
    }
    if (lineSection.duration >= duration) {
      return false;
    }
    return upStation.isSame(lineSection.getUpStation());
  }

  public boolean canSplitDown(LineSection2 lineSection) {
    if (lineSection.distance >= distance) {
      return false;
    }
    if (lineSection.duration >= duration) {
      return false;
    }
    return downStation.isSame(lineSection.getDownStation());
  }

  public boolean isSame(LineSection2 lineSection) {
    return upStation.isSame(lineSection.getUpStation())
        && downStation.isSame(lineSection.getDownStation())
        && distance == lineSection.getDistance()
        && duration == lineSection.getDuration();
  }

  public List<LineSection2> split(LineSection2 lineSection) {
    if (canSplitUp(lineSection)) {
      return List.of(
          LineSection2.of(
              upStation, lineSection.getDownStation(), lineSection.distance, lineSection.duration),
          LineSection2.of(
              lineSection.getDownStation(),
              downStation,
              distance - lineSection.distance,
              duration - lineSection.duration));
    }
    if (canSplitDown(lineSection)) {
      return List.of(
          LineSection2.of(
              upStation,
              lineSection.upStation,
              distance - lineSection.distance,
              duration - lineSection.duration),
          LineSection2.of(
              lineSection.upStation, downStation, lineSection.distance, lineSection.duration));
    }
    throw new IllegalArgumentException("LineSection#split 가 가능하지 않습니다.");
  }

  public boolean contains(Station station) {
    return upStation.isSame(station) || downStation.isSame(station);
  }

  public LineSection2 merge(LineSection2 lineSection) {
    if (canAppend(lineSection)) {
      return LineSection2.of(
          upStation,
          lineSection.getDownStation(),
          distance + lineSection.distance,
          duration + lineSection.duration);
    }
    if (canPrepend(lineSection)) {
      return LineSection2.of(
          lineSection.getUpStation(),
          downStation,
          distance + lineSection.distance,
          duration + lineSection.duration);
    }
    throw new IllegalArgumentException("LineSection#merge 가 가능하지 않습니다.");
  }
}
