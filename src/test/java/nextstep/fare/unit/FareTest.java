package nextstep.fare.unit;

import nextstep.fare.domain.Fare;
import nextstep.fare.domain.LessThanMinimumDistanceException;
import org.assertj.core.api.ThrowableAssert.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("요금 단위 테스트")
public class FareTest {

    private static final long BASE_FARE = 1250L;

    @DisplayName("요금 생성 함수는, 1미만의 거리가 입력되면 예외가 발생한다.")
    @Test
    void createFareExceptionTest() {
        // when
        ThrowingCallable actual = () -> Fare.from(0);

        // then
        assertThatThrownBy(actual).isInstanceOf(LessThanMinimumDistanceException.class);
    }

    @DisplayName("요금 생성 함수는, 거리 1에서 10까지는 기본 요금을 생성한다.")
    @ValueSource(ints = {1, 5, 10})
    @ParameterizedTest
    void calculateFareTest(int distance) {
        assertFareForDistance(distance, BASE_FARE);
    }

    @DisplayName("요금 생성 함수는, 거리 11에서 50까지는 초과된 거리 5마다 100원이 추가된 요금을 생성한다.")
    @CsvSource({
            "11, 1350",
            "15, 1350",
            "16, 1450",
            "50, 2050"
    })
    @ParameterizedTest
    void calculateAdditionalTest(int distance, Long expected) {
        assertFareForDistance(distance, expected);
    }

    @DisplayName("요금 생성 함수는, 거리 50초과는 50까지 계산된 요금에 초과된 거리 8마다 100원이 추가된 요금을 생성한다.")
    @CsvSource({
            "51, 2150",
            "58, 2150",
            "59, 2250",
            "66, 2250",
            "67, 2350"
    })
    @ParameterizedTest
    void calculateReducedAdditionalFare(int distance, Long expected) {
        assertFareForDistance(distance, expected);
    }

    // 중복된 요금 검증 로직 분리
    private void assertFareForDistance(int distance, Long expectedFare) {
        // when
        Fare fare = Fare.from(distance);

        // then
        assertThat(fare).isEqualTo(new Fare(expectedFare));
    }
}
