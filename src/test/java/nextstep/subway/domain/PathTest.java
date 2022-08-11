package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class PathTest {

    private Station 강남역;

    private Station 역삼역;

    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        이호선 = new Line("이호선", "초록");
    }

    @DisplayName("경로의 요금을 계산할 수 있다. (10km 이내)")
    @Test
    void extractFare() {
        //given
        이호선.addSection(강남역, 역삼역, 5, 4);
        Path 경로 = new Path(이호선의구간들());

        //when
        int 요금 = 경로.extractFare();

        //then
        assertThat(요금).isEqualTo(1250);
    }

    @DisplayName("경로의 요금을 계산할 수 있다. (10km 초과 ~ 50km 이내)")
    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5, 6, 7, 8, 9, 10})
    void extractFareOver10KmUnder50Km(int multiply) {
        //given
        int 거리 = 5 * multiply;
        이호선.addSection(강남역, 역삼역, 거리, 4);
        Path 경로 = new Path(이호선의구간들());

        //when
        int 요금 = 경로.extractFare();

        //then
        assertThat(요금).isEqualTo(1250 + (100 * (multiply - 2)));
    }

    private Sections 이호선의구간들() {
        return new Sections(이호선.getSections());
    }

}