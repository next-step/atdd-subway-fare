package nextstep.subway.line.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.dto.LineSectionRequest2;
import nextstep.subway.line.domain.*;
import nextstep.subway.station.application.StationReader;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LineSectionService2 {
  private final LineSectionMapper2 lineSectionMapper;
  private final StationReader stationReader;
  private final LineService2 lineService;

  @Transactional
  public Line2 appendLineSection(Long lineId, LineSectionRequest2 request) {
    Line2 line = lineService.findLineById(lineId);
    LineSection2 lineSection = lineSectionMapper.map(request);
    line.addLineSection(lineSection);
    return line;
  }

  @Transactional
  public void removeLineSection(Long lineId, Long stationId) {
    Line2 line = lineService.findLineById(lineId);
    Station station = stationReader.readById(stationId);
    line.remove(station);
  }
}
