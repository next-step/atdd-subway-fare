package nextstep.subway.domain.chain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static nextstep.subway.domain.chain.BasicFareHandler.BASIC_FARE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BasicFareHandlerTest {

    @Autowired
    private BasicFareHandler basicFareHandler;

    @ParameterizedTest
    @ValueSource(longs = {1, 10})
    void 이용거리가_10km_이내_일경우_기본운임요금인_1250원을_반환한다(long distance) {
        // when
        long fare = basicFareHandler.calculate(distance);

        // then
        assertEquals(BASIC_FARE, fare);
    }

}
