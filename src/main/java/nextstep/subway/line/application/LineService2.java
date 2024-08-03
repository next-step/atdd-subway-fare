package nextstep.subway.line.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.dto.LineRequest2;
import nextstep.subway.line.application.dto.UpdateLineRequest;
import nextstep.subway.line.domain.*;
import nextstep.subway.line.exception.LineNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LineService2 {
  private final LineSectionMapper2 lineSectionMapper;
  private final LineRepository2 lineRepository;

  @Transactional
  public Line2 saveLine(LineRequest2 request) {
    Line2 line = lineRepository.save(request.toLine());
    LineSection2 lineSection = lineSectionMapper.map(request.toLineSection());
    line.addLineSection(lineSection);
    return line;
  }

  public List<Line2> findAllLines() {
    return lineRepository.findAll();
  }

  public Line2 findLineById(Long id) {
    return lineRepository.findById(id).orElseThrow(() -> new LineNotFoundException(id));
  }

  @Transactional
  public Line2 updateLineById(Long id, UpdateLineRequest request) {
    Line2 line = findLineById(id);
    line.changeName(request.getName());
    line.changeColor(request.getColor());
    return line;
  }

  public void deleteLineById(Long id) {
    lineRepository.deleteById(id);
  }
}
