package nextstep.subway.unit;

import nextstep.subway.domain.FareStandard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @DisplayName("거리에 따른 요금 계산 - 10km까지 기본요금 1250원, 11km ~ 50km까지 5km당 100원 추가, 51km 이상부터 8km당 100원 추가")
    @ParameterizedTest
    @CsvSource({"10, 1250", "15, 1350", "16, 1450", "50, 2050", "58, 2150", "59, 2250"})
    void calculateOverFare(int inputDistance, int expectedFare) {
        // when
        int fareCalOver = FareStandard.calculateOverFare(inputDistance);

        // then
        assertThat(fareCalOver).isEqualTo(expectedFare);
    }
}
