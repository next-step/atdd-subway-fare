package nextstep.subway.unit;

import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTest {
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 판교역;
    private Line 이호선;
    private Line 신분당선;
    private Path 신분당선_구간;
    private Path 신분당선_구간2;
    private Path 신분당선_이호선_구간;

    /**
     * 교대역    --- *2호선* ---   강남역
     *                            |
     *                         *신분당선*
     *                            |
     *                          양재역
     */
    @BeforeEach
    public void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        판교역 = new Station("판교역");

        이호선 = new Line("2호선", "green",1200);
        신분당선 = new Line("신분당선", "red", 900);

        신분당선_구간 = new Path(new Sections(List.of(
                new Section(신분당선, 강남역, 양재역, 4, 4)
        )));

        신분당선_구간2 = new Path(new Sections(List.of(
                new Section(신분당선, 양재역, 판교역, 11, 4)
        )));

        신분당선_이호선_구간 = new Path(new Sections(List.of(
                new Section(이호선, 교대역, 강남역, 4, 4),
                new Section(신분당선, 강남역, 양재역, 4, 4)
        )));
    }

    @DisplayName("10km 미만 추가 요금이 있는 노선이 포함된 지하철 구간의 요금을 구한다.")
    @Test
    void extractFareWithExtraFare() {
        // when & then
        assertThat(신분당선_구간.extractFare()).isEqualTo(2150);
    }

    @DisplayName("10km 이상 추가 요금이 있는 노선이 포함된 지하철 구간의 요금을 구한다.")
    @Test
    void extractFare10kmWithExtraFare() {
        // when & then
        assertThat(신분당선_구간2.extractFare()).isEqualTo(2250);
    }

    @DisplayName("추가 요금이 있는 여러 노선이 포함된 지하철 구간일 경우, 추가 요금은 추가 요금 중 가장 높은 금액으로 적용한다.")
    @Test
    void extraFareWithExtraFareLines() {
        // when & then
        assertThat(신분당선_이호선_구간.extractFare()).isEqualTo(2450);
    }
}
