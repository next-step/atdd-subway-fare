package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AgeOfFareTypeTest {

    @DisplayName("원하는 할인 요금이 나온다.")
    @Test
    void ageFareType() {

        // given
        int childrenFare = AgeOfFareType.calculate(1250, 6);

        // then
        assertThat(childrenFare).isEqualTo(450);

        // given
        int teenagerFare = AgeOfFareType.calculate(1250, 15);

        // then
        assertThat(teenagerFare).isEqualTo(720);

        // given
        int adultFare = AgeOfFareType.calculate(1250, 20);

        // then
        assertThat(adultFare).isEqualTo(1250);
    }
}
