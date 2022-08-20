package nextstep.subway.unit;

import nextstep.subway.domain.SubwayFarePolicyType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayFarePolicyTypeTest {
    private SubwayFarePolicyType subwayFarePolicyType;
    @DisplayName("유형별 객체 테스트")
    @ParameterizedTest
    @EnumSource
    void testEnum(SubwayFarePolicyType subwayFarePolicyType){

    }

    @DisplayName("입력 나이에 따라 올바른 객체 가져오는지 테스트")
    @ParameterizedTest
    @CsvSource({
            "36, ADULT",
            "15, YOUTH",
            "8, CHILD"
    })
    void testEnumObectByAge(int inputAge, String person){
        subwayFarePolicyType = SubwayFarePolicyType.byAge(inputAge);
        assertThat(subwayFarePolicyType).isEqualTo(SubwayFarePolicyType.valueOf(person));
    }

}
