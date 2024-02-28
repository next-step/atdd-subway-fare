package nextstep.subway.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FareAgeGroupTest {

    @Test
    void 어린이일_경우_요금에서_350원_제외하구_남은_금액의_50퍼센트_할인을_받는다(){
        FareAgeGroup child = FareAgeGroup.CHILD;
        int discountFare = child.calculateFare(1000);
        assertThat(discountFare).isEqualTo(325);
    }

    @Test
    void 청소년일_경우_요금에서_350원_제외하구_남은_금액의_20퍼센트_할인을_받는다(){
        FareAgeGroup child = FareAgeGroup.TEENAGER;
        int discountFare = child.calculateFare(1000);
        assertThat(discountFare).isEqualTo(520);
    }

    @Test
    void 성인일경우_할인요금이_적용되지_않는다(){
        FareAgeGroup child = FareAgeGroup.ADULT;
        int discountFare = child.calculateFare(1000);
        assertThat(discountFare).isEqualTo(1000);
    }

}
