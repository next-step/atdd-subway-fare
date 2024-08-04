package nextstep.subway.path.application.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import nextstep.subway.path.domain.PathType;

@Getter
@EqualsAndHashCode(of = {"source", "target", "type"})
public class PathRequest {
  private final Long source;
  private final Long target;
  private final PathType type;

  private PathRequest(Long source, Long target, PathType type) {
    this.source = source;
    this.target = target;
    this.type = type;
  }

  public static PathRequest of(Long source, Long target, PathType type) {
    return new PathRequest(source, target, type);
  }
}
