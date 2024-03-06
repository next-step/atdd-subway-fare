package nextstep.subway.applicaion;

import nextstep.subway.domain.BaseFareCalculator;
import nextstep.subway.domain.Over10kmSurchargeCalculator;
import nextstep.subway.domain.Over50kmSurchargeCalculator;
import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

  private final BaseFareCalculator basicCalculator = new BaseFareCalculator();
  private final Over10kmSurchargeCalculator over10kmSurchargeCalculator = new Over10kmSurchargeCalculator();
  private final Over50kmSurchargeCalculator over50kmSurchargeCalculator = new Over50kmSurchargeCalculator();

  public int calculate(final int distance) {
    return basicCalculator.calculate()
        + over10kmSurchargeCalculator.calculate(distance)
        + over50kmSurchargeCalculator.calculate(distance);
  }
}
