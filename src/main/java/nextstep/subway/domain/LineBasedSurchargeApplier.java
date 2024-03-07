package nextstep.subway.domain;

import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LineBasedSurchargeApplier extends FareCalculator {

  private final List<Line> lines;

  @Override
  public Fare calculate(Fare fare) {
    lines.stream()
        .mapToInt(Line::getExtraFare)
        .max()
        .ifPresent(fare::addSurcharge);

    return fare;
  }
}
