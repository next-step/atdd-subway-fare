package nextstep.subway.domain.path;

public enum PathType {

  DISTANCE("거리"),
  DURATION("시간");

  private String description;

  PathType(String description) {
    this.description = description;
  }
}
