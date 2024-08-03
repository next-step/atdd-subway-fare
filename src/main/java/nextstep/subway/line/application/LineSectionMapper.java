package nextstep.subway.line.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.dto.LineSectionRequest;
import nextstep.subway.line.domain.LineSection2;
import nextstep.subway.station.application.StationReader;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LineSectionMapper {
  private final StationReader stationReader;

  public LineSection2 map(LineSectionRequest request) {
    Station upStation = stationReader.readById(request.getUpStationId());
    Station downStation = stationReader.readById(request.getDownStationId());
    return new LineSection2(upStation, downStation, request.getDistance(), request.getDuration());
  }
}
