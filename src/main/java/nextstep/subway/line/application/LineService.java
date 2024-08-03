package nextstep.subway.line.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.UpdateLineRequest;
import nextstep.subway.line.domain.*;
import nextstep.subway.line.exception.LineNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LineService {
  private final LineSectionMapper lineSectionMapper;
  private final LineRepository lineRepository;

  @Transactional
  public Line saveLine(LineRequest request) {
    Line line = lineRepository.save(request.toLine());
    LineSection lineSection = lineSectionMapper.map(request.toLineSection());
    line.addLineSection(lineSection);
    return line;
  }

  public List<Line> findAllLines() {
    return lineRepository.findAll();
  }

  public Line findLineById(Long id) {
    return lineRepository.findById(id).orElseThrow(() -> new LineNotFoundException(id));
  }

  @Transactional
  public Line updateLineById(Long id, UpdateLineRequest request) {
    Line line = findLineById(id);
    line.changeName(request.getName());
    line.changeColor(request.getColor());
    return line;
  }

  public void deleteLineById(Long id) {
    lineRepository.deleteById(id);
  }
}
