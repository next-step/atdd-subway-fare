package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.fare.Fare;
import org.junit.jupiter.api.Test;

class FareTest {

  @Test
  void addSurcharge() {
    // given
    var fare = Fare.baseFare();
    var surcharge = 300;

    // when
    fare.addSurcharge(surcharge);

    // then
    assertThat(fare.getSurcharge()).isEqualTo(surcharge);
    assertThat(fare.getTotalFare()).isEqualTo(Fare.baseFare().getTotalFare() + surcharge);
  }

  @Test
  void applyDiscount() {
    // given
    var fare = Fare.baseFare();
    var discountAmount = 500;

    // when
    fare.applyDiscount(discountAmount);

    // then
    assertThat(fare.getDiscountAmount()).isEqualTo(discountAmount);
    assertThat(fare.getTotalFare()).isEqualTo(Fare.baseFare().getTotalFare() - discountAmount);
  }

  @Test
  void getTotalFare() {
    // given
    var fare = Fare.baseFare();
    var surcharge = 300;
    var discountAmount = 500;

    // when
    fare.addSurcharge(surcharge);
    fare.applyDiscount(discountAmount);

    // then
    assertThat(fare.getDiscountAmount()).isEqualTo(discountAmount);
    assertThat(fare.getTotalFare()).isEqualTo(Fare.baseFare().getTotalFare() + surcharge - discountAmount);
  }
}