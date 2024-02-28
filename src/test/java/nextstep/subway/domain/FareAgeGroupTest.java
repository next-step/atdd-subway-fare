package nextstep.subway.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FareAgeGroupTest {

    @Test
    void 어린이일_경우_요금에서_350원_제외하구_남은_금액의_50퍼센트_할인을_받는다(){
        FareAgeGroup child = FareAgeGroup.CHILD;
        long discountFare = child.calculateDiscountFare(1000);
        assertThat(discountFare).isEqualTo(325);
    }

    @Test
    void 청소년일_경우_요금에서_350원_제외하구_남은_금액의_20퍼센트_할인을_받는다(){
        FareAgeGroup teenager = FareAgeGroup.TEENAGER;
        long discountFare = teenager.calculateDiscountFare(1000);
        assertThat(discountFare).isEqualTo(130);
    }

    @Test
    void 성인일경우_할인요금이_적용되지_않는다(){
        FareAgeGroup adult = FareAgeGroup.ADULT;
        long discountFare = adult.calculateDiscountFare(1000);
        assertThat(discountFare).isEqualTo(0);
    }

}
