package nextstep.subway.path.domain.policy;

public interface FarePolicy {

  long calculate(int remainDistance);


  default int calculateOverFare(int distance,int perKilometer) {
    return (int) ((Math.ceil((distance - 1) / perKilometer) + 1) * 100);
  }


}
