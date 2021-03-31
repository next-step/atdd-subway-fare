package nextstep.subway.path.domain.policy;

public class BasicFarePolicy implements FarePolicy {

  private static final int DEFAULT_COST = 1250;

  @Override
  public long calculate(int remainDistance) {
    return DEFAULT_COST;
  }

}
