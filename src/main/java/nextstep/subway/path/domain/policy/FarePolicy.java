package nextstep.subway.path.domain.policy;

public interface FarePolicy {
  void setNextPolicy(FarePolicy farePolicy);
  long calculate(int remainDistance,long price);
  default int calculateOverFare(int distance,int perKilometer) {
    return (int) ((Math.ceil((distance - 1) / perKilometer) + 1) * 100);
  }
}
