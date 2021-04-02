package nextstep.subway.path.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("가격 연산")
public class FareTest {

    @DisplayName("가격 덧셈")
    @Test
    public void sumFareTest() {
        // given
        Fare one = Fare.of(1);
        Fare two = Fare.of(1);

        // when
        Fare fare = Fare.sum(one, two);

        // then
        assertThat(fare).isEqualTo(Fare.of(2));
    }

    @DisplayName("숫자형 변환확인")
    @Test
    public void parseIntTest() {
        // given
        Fare one = Fare.of(1);
        Fare two = Fare.of(1);

        // when
        Fare fare = Fare.sum(one, two);

        // then
        assertThat(Fare.parseInt(fare)).isEqualTo(2);
    }
}
