package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class PathTest {

    @DisplayName("경로의 요금을 계산할 수 있다. (10km 이내)")
    @Test
    void extractFare() {
        //given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 선릉역 = new Station("선릉역");

        Line 이호선 = new Line("이호선", "초록");
        이호선.addSection(강남역, 역삼역, 5, 4);
        이호선.addSection(역삼역, 선릉역, 4, 5);

        Path 경로 = new Path(new Sections(이호선.getSections()));

        //when
        int 요금 = 경로.extractFare();

        //then
        assertThat(요금).isEqualTo(1250);
    }

    @DisplayName("경로의 요금을 계산할 수 있다. (10km 초과 ~ 50km 이내)")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
    void extractFareOver10KmUnder50Km(int multiply) {
        //given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 선릉역 = new Station("선릉역");
        Station 삼성역 = new Station("삼성역");

        Line 이호선 = new Line("이호선", "초록");
        이호선.addSection(강남역, 역삼역, 5, 4);
        이호선.addSection(역삼역, 선릉역, 4, 5);
        이호선.addSection(선릉역, 삼성역, 5 * multiply, 5);

        Path 경로 = new Path(new Sections(이호선.getSections()));

        //when
        int 요금 = 경로.extractFare();

        //then
        assertThat(요금).isEqualTo(1250 + 100 * multiply);
    }

}