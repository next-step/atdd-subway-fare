package nextstep.subway.domain.vo;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;

@Getter
@AllArgsConstructor
public class Path {

  private List<StationResponse> vertices;
  private int distance;
  private int duration;
  private List<Long> lines;

  public static Path from(List<Station> vertices, int distance, int duration, List<Long> lines) {
    return new Path(
        vertices.stream()
            .map(StationResponse::from)
            .collect(Collectors.toList()),
        distance,
        duration,
        lines
    );
  }
}
