package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.domain.SubwayFarePolicyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SubwayFarePolicyTypeTest {

    @DisplayName("유형을 입력했을때 유형이 가지고 있는 값 확인")
    @ParameterizedTest
    @CsvSource({
            "YOUTH, 13, 18, 0.2",
            "CHILD, 6, 12, 0.5",
            "ADULT, 19, 100, 0",
    })
    void getValuePerType(String type, int min, int max, double percent) {
        SubwayFarePolicyType subwayFarePolicyType = SubwayFarePolicyType.valueOf(type);

        assertThat(subwayFarePolicyType.getMinAge()).isEqualTo(min);
        assertThat(subwayFarePolicyType.getMaxAge()).isEqualTo(max);
        assertThat(subwayFarePolicyType.getDiscountPercent()).isEqualTo(percent);


    }

}
