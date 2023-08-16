package nextstep.line.domain;

import nextstep.exception.FareNotMatchException;
import nextstep.line.domain.fare.DistanceFarePolicies;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FareTest {

    private final DistanceFarePolicies distanceFarePolicies = new DistanceFarePolicies();

    @DisplayName("이동거리에 따라 알맞는 요금이 산출되야한다.")
    @ParameterizedTest
    @CsvSource(value = {"3,1250","9,1250","12,1350","16,1450","52,2150"}, delimiterString = ",")
    void 요금산출(int distance, int fare) {
        // when then
        assertThat(distanceFarePolicies.getFare(distance)).isEqualTo(fare);
    }

    @DisplayName("이동거리가 비정상일 경우 에러가 발생한다.")
    @Test
    void 요금산출_비정상거리() {
        // given
        int distance = -1;
        // when then
        assertThatThrownBy(() -> distanceFarePolicies.getFare(distance))
                .isExactlyInstanceOf(FareNotMatchException.class)
                .hasMessage("요금 산출에 실패했습니다.");
    }
}
