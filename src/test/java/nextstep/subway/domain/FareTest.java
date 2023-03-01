package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    private Fare fare;

    @BeforeEach
    void setUp() {
        fare = new Fare();
    }

    /**
     * When 거리 비례 요금 계산 요청 시
     * Then 계산이 된다
     */
    @DisplayName("거리_비례_요금_계산_요청_시_계산이_된다")
    @Test
    void 요금_계산_요청_시_계산이_된다() {
        // When
        fare.addPolicy(new DistanceFarePolicy(10));

        // Then
        int fee = fare.calcFare();
        assertThat(fee).isEqualTo(1250);
    }

    /**
     * When 나이 비례 요금 계산 요청 시
     * Then 계산이 된다
     */
    @ParameterizedTest
    @CsvSource({
            "5, 1250",
            "12, 450",
            "13, 720",
            "18, 720",
            "19, 1250"
    })
    @DisplayName("나이_비례_요금_계산_요청_시_계산이_된다")
    void 나이_비례_계산_요청_시_계산이_된다(int age, int expectedFee) {
        // When
        fare.addPolicy(new AgeFarePolicy(age));

        // Then
        int fee = fare.calcFare();
        assertThat(fee).isEqualTo(expectedFee);
    }

    /**
     * When 나이 및 거리 비례 요금 계산 요청 시
     * Then 계산이 된다
     */
    @ParameterizedTest
    @CsvSource({
            "5,  10, 1250",
            "12, 12,  500",     // (1250 + 100 - 350) / 2
            "13, 20,  720",     // (1250 + 200 - 350) * 0.8
            "18, 10,  720",     // (1250 - 350) * 0.8
            "19, 10,  1250"     // 1250
    })
    @DisplayName("나이 및 거리 비례 요금 계산 요청 시 계산이 된다")
    void 나이_및_거리_비례_요금_계산_요청_시_계산이_된다(int age, int distance, int expectedFee) {
        // When
        fare.addPolicy(new AgeFarePolicy(age));
        fare.addPolicy(new DistanceFarePolicy(distance));

        // Then
        int fee = fare.calcFare();
        assertThat(fee).isEqualTo(expectedFee);
    }

}