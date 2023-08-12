package nextstep.subway.unit;

import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubwayMapTest {

    public static final Station 강남역 = new Station("강남역");
    public static final Station 양재역 = new Station("양재역");
    public static final Station 교대역 = new Station("교대역");
    public static final Station 남부터미널역 = new Station("남부터미널역");

    Line 이호선;
    Line 삼호선;
    Line 신분당선;

    Section 강남_양재_구간;
    Section 교대_강남_구간;
    Section 교대_남부터미널_구간;
    Section 남부터미널_양재_구간;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        강남_양재_구간 = new Section(신분당선, 강남역, 양재역, 10, 2);
        신분당선.getSections().add(강남_양재_구간);

        이호선 = new Line("이호선", "green");
        교대_강남_구간 = new Section(이호선, 교대역, 강남역, 10, 2);
        이호선.getSections().add(교대_강남_구간);

        삼호선 = new Line("이호선", "orange");
        교대_남부터미널_구간 = new Section(삼호선, 교대역, 남부터미널역, 2, 10);
        남부터미널_양재_구간 = new Section(삼호선, 남부터미널역, 양재역, 3, 10);
        삼호선.getSections().add(교대_남부터미널_구간);
        삼호선.getSections().add(남부터미널_양재_구간);
    }

    @Test
    @DisplayName("최단 거리 경로 조회 테스트")
    void findShortestDistancePath() {
        List<Section> expected = List.of(교대_남부터미널_구간, 남부터미널_양재_구간);

        Path path = new SubwayMap(List.of(이호선, 삼호선, 신분당선)).findShortDistancePath(교대역, 양재역);

        assertThat(path.getSections().getSections()).isEqualTo(expected);
    }

    @Test
    @DisplayName("최단 시간 경로 조회 테스트")
    void findShortestDurationPath() {
        List<Section> expected = List.of(교대_강남_구간, 강남_양재_구간);

        Path path = new SubwayMap(List.of(이호선, 삼호선, 신분당선)).findShortDurationPath(교대역, 양재역);

        assertThat(path.getSections().getSections()).isEqualTo(expected);
    }
}
