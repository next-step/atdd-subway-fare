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
public class LineSection {
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
  public LineSection(Long id, Station upStation, Station downStation, int distance, int duration) {
    this.id = id;
    this.upStation = upStation;
    this.downStation = downStation;
    this.distance = distance;
    this.duration = duration;
  }

  public LineSection(Station upStation, Station downStation, int distance, int duration) {
    this(null, upStation, downStation, distance, duration);
  }

  public static LineSection of(Station upStation, Station downStation, int distance, int duration) {
    return new LineSection(upStation, downStation, distance, duration);
  }

  public boolean canPrepend(LineSection lineSection) {
    return upStation.isSame(lineSection.downStation);
  }

  public boolean canAppend(LineSection lineSection) {
    return downStation.isSame(lineSection.upStation);
  }

  public boolean canSplitUp(LineSection lineSection) {
    if (lineSection.distance >= distance) {
      return false;
    }
    if (lineSection.duration >= duration) {
      return false;
    }
    return upStation.isSame(lineSection.getUpStation());
  }

  public boolean canSplitDown(LineSection lineSection) {
    if (lineSection.distance >= distance) {
      return false;
    }
    if (lineSection.duration >= duration) {
      return false;
    }
    return downStation.isSame(lineSection.getDownStation());
  }

  public boolean isSame(LineSection lineSection) {
    return upStation.isSame(lineSection.getUpStation())
        && downStation.isSame(lineSection.getDownStation())
        && distance == lineSection.getDistance()
        && duration == lineSection.getDuration();
  }

  public List<LineSection> split(LineSection lineSection) {
    if (canSplitUp(lineSection)) {
      return List.of(
          LineSection.of(
              upStation, lineSection.getDownStation(), lineSection.distance, lineSection.duration),
          LineSection.of(
              lineSection.getDownStation(),
              downStation,
              distance - lineSection.distance,
              duration - lineSection.duration));
    }
    if (canSplitDown(lineSection)) {
      return List.of(
          LineSection.of(
              upStation,
              lineSection.upStation,
              distance - lineSection.distance,
              duration - lineSection.duration),
          LineSection.of(
              lineSection.upStation, downStation, lineSection.distance, lineSection.duration));
    }
    throw new IllegalArgumentException("LineSection#split 가 가능하지 않습니다.");
  }

  public boolean contains(Station station) {
    return upStation.isSame(station) || downStation.isSame(station);
  }

  public LineSection merge(LineSection lineSection) {
    if (canAppend(lineSection)) {
      return LineSection.of(
          upStation,
          lineSection.getDownStation(),
          distance + lineSection.distance,
          duration + lineSection.duration);
    }
    if (canPrepend(lineSection)) {
      return LineSection.of(
          lineSection.getUpStation(),
          downStation,
          distance + lineSection.distance,
          duration + lineSection.duration);
    }
    throw new IllegalArgumentException("LineSection#merge 가 가능하지 않습니다.");
  }
}
