package nextstep.subway.domain.farepolicy;

import nextstep.subway.domain.Path;

public class Fare {

  private static final int DEFAULT_FARE = 1250;
  private final Path path;
  private final FarePolicyGroup farePolicyGroup;
  private final DiscountFarePolicyGroup discountFarePolicyGroup;

  private Fare(Path path) {
    this.path = path;
    farePolicyGroup = FarePolicyGroup.of();
    discountFarePolicyGroup = DiscountFarePolicyGroup.of();
  }

  public static Fare of(Path path) {
    return new Fare(path);
  }

  public int calculateFare(int age) {
    int distance = path.extractDistance();
    int lineFare = path.extractLineFare();

    int additionalFare = farePolicyGroup.calculateFare(distance, lineFare);
    int sumTotal = discountFarePolicyGroup.calculateFare(DEFAULT_FARE + additionalFare, age);

    return sumTotal;
  }
}
