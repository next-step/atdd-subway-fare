package nextstep.subway.path.domain;

public class Fare {

  public static final int DEFAULT_COST = 1250;
  private long cost;

  public Fare(int totalDistance) {
    if(totalDistance > 0) {
      this.cost = calculate(totalDistance);
    }
  }

  public long getCost(){
    return this.cost;
  }

  private long calculate(int totalDistance){
    long additionalPriceOver10 = 0;
    long additionalPriceOver50 = 0;
    totalDistance-=10;

    if(totalDistance >= 5) {
      additionalPriceOver10 = Math.min(calculateOverFare(totalDistance,5),800);
    }
    if(totalDistance >= 45){
      totalDistance -= 50;
      additionalPriceOver50 = calculateOverFare(totalDistance,8);
    }

    return DEFAULT_COST + additionalPriceOver10 + additionalPriceOver50;
  }

  private int calculateOverFare(int distance,int perKilometer) {
    return (int) ((Math.ceil((distance - 1) / perKilometer) + 1) * 100);
  }


}
