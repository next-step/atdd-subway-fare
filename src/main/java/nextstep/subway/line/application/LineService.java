package nextstep.subway.line.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.Line;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LineService {
  private final LineReader lineReader;

  public List<Line> findAllLines() {
    return lineReader.read();
  }
}
