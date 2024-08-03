package nextstep.subway.line.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.dto.LineSectionRequest;
import nextstep.subway.line.domain.*;
import nextstep.subway.station.application.StationReader;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LineSectionService {
  private final LineSectionMapper lineSectionMapper;
  private final StationReader stationReader;
  private final LineService lineService;

  @Transactional
  public Line2 appendLineSection(Long lineId, LineSectionRequest request) {
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
