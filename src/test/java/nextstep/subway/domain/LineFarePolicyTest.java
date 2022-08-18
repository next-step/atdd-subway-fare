package nextstep.subway.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineFarePolicyTest {

    @DisplayName("노선에 추가요금이 있을 때 합산된다.")
    @Test
    void lineFareAdded() {
        //given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Line 신분당선 = new Line("신분당선", "red", 1000);

        Path path = new Path(new Sections(List.of(new Section(신분당선, 강남역, 양재역, 10, 10))));
        FarePolicy policy = new LineFarePolicy(path);

        //when
        int fare = policy.proceed(1250);

        //then
        assertThat(fare).isEqualTo(2250);
    }

    @DisplayName("노선에 추가요금이 없을 때 합산되지 않는다.")
    @Test
    void lineFareNoAdded() {
        //given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Line 이호선 = new Line("이호선", "green", 0);

        Path path = new Path(new Sections(List.of(new Section(이호선, 강남역, 역삼역, 10, 10))));
        FarePolicy policy = new LineFarePolicy(path);

        //when
        int fare = policy.proceed(1250);

        //then
        assertThat(fare).isEqualTo(1250);
    }
}