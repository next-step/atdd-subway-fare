package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static nextstep.subway.fixture.LineFixture.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class LinesTest {

    static Line 신분당선;
    static Line 분당선;
    static Line 일호선;
    static Line 이호선;
    static Line 삼호선;
    static Lines 추가요금이_없는_라인;
    static Lines 추가요금이_있는_라인;

    @BeforeAll
    static void setUp() {
        신분당선 = SHINBUNDANG_LINE.toLine(1L);
        분당선 = BUNDANG_LINE.toLine(2L);
        일호선 = ONE_LINE.toLine(3L);
        이호선 = TWO_LINE.toLine(4L);
        삼호선 = THREE_LINE.toLine(5L);

        추가요금이_없는_라인 = new Lines(List.of(분당선, 이호선));
        추가요금이_있는_라인 = new Lines(List.of(신분당선, 일호선, 삼호선));
    }

    @Test
    void 육세이하일경우_추가요금을_받지않아_0원을_반환한다() {
        long extraFare = 추가요금이_있는_라인.calculatePlusExtraFare(FareAgeGroup.PRESCHOOLERS);
        assertThat(extraFare).isEqualTo(0L);
    }

    @ParameterizedTest
    @EnumSource(value = FareAgeGroup.class, names = {"CHILD", "TEENAGER", "ADULT"})
    void 추가요금이_없는_노선일_경우_0원을_반환한다(FareAgeGroup fareAgeGroup) {
        long extraFare = 추가요금이_없는_라인.calculatePlusExtraFare(fareAgeGroup);
        assertThat(extraFare).isEqualTo(0L);
    }

    @Test
    @EnumSource(value = FareAgeGroup.class, names = {"CHILD", "TEENAGER", "ADULT"})
    void 추가요금이_있는_노선일_경우_가장_큰_금액의_추가요금을_반환한다() {
        long extraFare = 추가요금이_있는_라인.calculatePlusExtraFare(FareAgeGroup.ADULT);
        assertThat(extraFare).isEqualTo(1500L);
    }

}
