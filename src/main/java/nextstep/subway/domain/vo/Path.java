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

  public static Path from(List<Station> vertices, int distance, int duration) {
    return new Path(
        vertices.stream()
            .map(StationResponse::from)
            .collect(Collectors.toList()),
        distance,
        duration
    );
  }
}
