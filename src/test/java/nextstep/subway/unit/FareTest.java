package nextstep.subway.unit;

import lombok.val;
import nextstep.subway.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @DisplayName("10KM이하 요금테스트")
    @Test
    void 최소범위테스트() {
        // when
        val fare1 = Fare.calculateAmount(9, 0);
        val fare2 = Fare.calculateAmount(10, 0);

        assertThat(fare1).isEqualTo(1250);
        assertThat(fare2).isEqualTo(1250);
    }

    @DisplayName("10KM초과 50KM 이하 요금테스트")
    @Test
    void 중간범위테스트() {
        // when
        val fare1 = Fare.calculateAmount(11, 0);
        val fare2 = Fare.calculateAmount(16, 0);
        val fare3 = Fare.calculateAmount(50, 0);
        val extraFare = Fare.calculateAmount(50, 100);

        assertThat(fare1).isEqualTo(1350);
        assertThat(fare2).isEqualTo(1450);
        assertThat(fare3).isEqualTo(2050);
        assertThat(extraFare).isEqualTo(2150);
    }

    @DisplayName("50KM초과 요금테스트")
    @Test
    void 최대범위테스트() {
        // when
        val fare1 = Fare.calculateAmount(51, 0);
        val fare2 = Fare.calculateAmount(57, 0);
        val fare3 = Fare.calculateAmount(60, 0);

        assertThat(fare1).isEqualTo(2150);
        assertThat(fare2).isEqualTo(2150);
        assertThat(fare3).isEqualTo(2250);
    }
}
