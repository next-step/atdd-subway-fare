package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.PathResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class FareCalculatorTest {

    private static final int 기본_요금 = 1250;

    FareCalculator fareCalculator = new FareCalculator();

    @Test
    void 이용_거리가_0이면_요금도_0원이다() {
        거리에_따른_가격_비교(0, 0);
    }

    @Test
    void 이용_거리가_10km이하는_기본_요금이_나와야_한다() {
        거리에_따른_가격_비교(8, 기본_요금);
        거리에_따른_가격_비교(10, 기본_요금);
    }

    /**
     * 11km = 10km + 1km = 기본_요금 + ceil(1/5)*100
     * 37km = 10km + 27km = 기본_요금 + ceil(27/5)*100
     * 50km = 10km + 40km = 기본_요금 + ceil(40/5)*100
     */
    @Test
    void 이용_거리가_10km초과_50km이하는_5km마다_100원의_추가요금이_나와야_한다() {
        거리에_따른_가격_비교(11, 기본_요금 + 100);
        거리에_따른_가격_비교(37, 기본_요금 + 600);
        거리에_따른_가격_비교(50, 기본_요금 + 800);
    }

    /**
     * 51km = 10km + 40km + 1km = 기본_요금 + 8*100 + ceil(1/8)*100
     * 107km = 10km + 40km + 56km = 기본_요금 + 8*100 + ceil(57/8)*100
     */
    @Test
    void 이용_거리가_50km초과분은_8km마다_100원의_추가요금이_나와야_한다() {
        거리에_따른_가격_비교(51, 기본_요금 + 800 + 100);
        거리에_따른_가격_비교(107, 기본_요금 + 800 + 800);
    }

    private void 거리에_따른_가격_비교(int distance, int price) {
        int calculatedFare = fareCalculator.calculateOverFare(distance);

        assertThat(calculatedFare).isEqualTo(price);
    }
}
