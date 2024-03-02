package nextstep.subway.domain.chain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.domain.chain.BasicFareHandler.BASIC_FARE;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class BasicFareHandlerTest {

    private BasicFareHandler basicFareHandler;

    @BeforeEach
    void setUp(){
        basicFareHandler = new BasicFareHandler();
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 10})
    void 이용거리가_10km_이내_일경우_기본운임요금인_1250원을_반환한다(long distance) {
        // when
        long fare = basicFareHandler.calculate(distance);

        // then
        assertThat(fare).isEqualTo(BASIC_FARE);
    }

}
