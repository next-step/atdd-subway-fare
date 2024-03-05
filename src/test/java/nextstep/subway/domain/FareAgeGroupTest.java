package nextstep.subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FareAgeGroupTest {

    @Test
    void 오세이하일_경우_100퍼센트_할인을_받는다() {
        FareAgeGroup child = FareAgeGroup.PRESCHOOLERS;
        long discountFare = child.calculateDiscountFare(1000);
        assertThat(discountFare).isEqualTo(1000);
    }

    @Test
    void 어린이일_경우_요금에서_350원_제외하구_남은_금액의_50퍼센트_할인을_받는다() {
        FareAgeGroup child = FareAgeGroup.CHILD;
        long discountFare = child.calculateDiscountFare(1000);
        assertThat(discountFare).isEqualTo(325);
    }

    @Test
    void 청소년일_경우_요금에서_350원_제외하구_남은_금액의_20퍼센트_할인을_받는다() {
        FareAgeGroup teenager = FareAgeGroup.TEENAGER;
        long discountFare = teenager.calculateDiscountFare(1000);
        assertThat(discountFare).isEqualTo(130);
    }

    @Test
    void 성인일경우_할인요금이_적용되지_않는다() {
        FareAgeGroup adult = FareAgeGroup.ADULT;
        long discountFare = adult.calculateDiscountFare(1000);
        assertThat(discountFare).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("provideFareAgeGroup")
    void 연령별_노선추가요금의_무료요금_여부를_검증한다(FareAgeGroup fareAgeGroup, boolean expected) {
        assertThat(fareAgeGroup.isLineExtraFareFree()).isEqualTo(expected);
    }

    private static Stream<Arguments> provideFareAgeGroup() {
        return Stream.of(
                Arguments.of(FareAgeGroup.PRESCHOOLERS, true),
                Arguments.of(FareAgeGroup.CHILD, false),
                Arguments.of(FareAgeGroup.TEENAGER, false),
                Arguments.of(FareAgeGroup.ADULT, false)
        );
    }


}
