package nextstep.subway.domain.fare;

import lombok.Getter;

@Getter
public class Fare {

  // 기본요금
  private static int BASE_FARE = 1250;

  private final int baseFare;

  private int surcharge;

  private int discountAmount;

  public void addSurcharge(int surcharge) {
    this.surcharge += surcharge;
  }

  public void applyDiscount(int discountAmount) {
    this.discountAmount += discountAmount;
  }

  public int getTotalFare() {
    return baseFare
        + surcharge
        - discountAmount;
  }

  public static Fare baseFare() {
    return new Fare(BASE_FARE);
  }

  private Fare(final int baseFare) {
    this.baseFare = baseFare;
  }
}
