package nextstep.subway.unit;

import nextstep.common.exception.SectionNotFoundException;
import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathTest {
    private Station 강남역;
    private Station 양재역;
    private Station 양재시민의숲역;
    private Station 남부터미널역;
    private Station 교대역;
    private Station 고속터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Section 신분당선_강남_양재;
    private Section 신분당선_양재_양재시민의숲;
    private Section 이호선_교대_강남;
    private Section 삼호선_교대_남부터미널;
    private Section 삼호선_남부터미널_양재;
    private Section 삼호선_양재_고속터미널;
    private List<Section> 신분당선_전체_구간;
    private List<Section> 여러노선_포함_구간;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        양재시민의숲역 = new Station("양재시민의숲역");
        남부터미널역 = new Station("남부터미널역");
        교대역 = new Station("교대역");
        고속터미널역 = new Station("고속터미널역");

        신분당선 = new Line("신분당선", "red", 900);
        이호선 = new Line("2호선", "green", 0);
        삼호선 = new Line("3호선", "orange", 500);

        신분당선_강남_양재 = Section.createSection(신분당선, 강남역, 양재역, 8, 10);
        신분당선_양재_양재시민의숲 = Section.createSection(신분당선, 양재역, 양재시민의숲역, 3, 3);
        이호선_교대_강남 = Section.createSection(이호선, 교대역, 강남역, 2, 3);
        삼호선_교대_남부터미널 = Section.createSection(삼호선, 교대역, 남부터미널역, 2, 3);
        삼호선_남부터미널_양재 = Section.createSection(삼호선, 남부터미널역, 양재역, 2, 3);
        삼호선_양재_고속터미널 = Section.createSection(삼호선, 양재역, 고속터미널역, 2, 3);

        신분당선_전체_구간 = Arrays.asList(신분당선_강남_양재, 신분당선_양재_양재시민의숲);
        여러노선_포함_구간 = new ArrayList<>(Arrays.asList(
                이호선_교대_강남, 신분당선_강남_양재, 삼호선_교대_남부터미널, 삼호선_남부터미널_양재
        ));
    }

    @Test
    @DisplayName("경로 생성 및 기본 속성 테스트")
    void createPath() {
        List<Station> 경로_역_목록 = Arrays.asList(강남역, 양재역, 양재시민의숲역);
        Path 경로 = Path.createPath(경로_역_목록, 신분당선_전체_구간, PathType.DISTANCE, 11, 20);

        assertThat(경로.getStations()).containsExactly(강남역, 양재역, 양재시민의숲역);
        assertThat(경로.getPathType()).isEqualTo(PathType.DISTANCE);
        assertThat(경로.getTotalWeight()).isEqualTo(11);
        assertThat(경로.getDistance()).isEqualTo(11);
        assertThat(경로.getDuration()).isEqualTo(13);
    }

    @Test
    @DisplayName("요금 계산 테스트 - 기본 요금")
    void calculateFareBasic() {
        List<Station> 경로_역_목록 = Arrays.asList(교대역, 강남역);
        Path 경로 = Path.createPath(경로_역_목록, Arrays.asList(이호선_교대_강남), PathType.DISTANCE, 2, 20);

        assertThat(경로.getFare()).isEqualTo(1250);  // 기본 요금 1250
    }

    @Test
    @DisplayName("요금 계산 테스트 - 거리 추가 요금")
    void calculateFareExtraDistance() {
        List<Station> 경로_역_목록 = Arrays.asList(강남역, 양재역);
        Path 경로 = Path.createPath(경로_역_목록, Arrays.asList(신분당선_강남_양재), PathType.DISTANCE, 8, 20);

        assertThat(경로.getFare()).isEqualTo(2150);  // 기본 요금 1250 + 신분당선 추가 요금 900
    }

    @Test
    @DisplayName("요금 계산 테스트 - 노선 추가 요금")
    void calculateFareExtraLine() {
        List<Station> 경로_역_목록 = Arrays.asList(교대역, 남부터미널역, 양재역);
        List<Section> 경로_구간_목록 = Arrays.asList(삼호선_교대_남부터미널, 삼호선_남부터미널_양재);
        Path 경로 = Path.createPath(경로_역_목록, 경로_구간_목록, PathType.DISTANCE, 4, 20);

        assertThat(경로.getFare()).isEqualTo(1750);  // 기본 요금 1250 + 3호선 추가 요금 500
    }

    @Test
    @DisplayName("요금 계산 테스트 - 장거리 및 노선 추가 요금")
    void calculateFareLongDistanceAndExtraLine() {
        List<Station> 경로_역_목록 = Arrays.asList(강남역, 양재역, 양재시민의숲역);
        Path 경로 = Path.createPath(경로_역_목록, 신분당선_전체_구간, PathType.DISTANCE, 11, 20);

        assertThat(경로.getFare()).isEqualTo(2250);  // 기본 요금 1250 + 거리 추가 요금 100 + 신분당선 추가 요금 900
    }

    @Test
    @DisplayName("여러 노선을 걸치는 경로 요금 계산")
    void calculateFareForMultipleLines() {
        List<Station> 경로_역_목록 = Arrays.asList(교대역, 강남역, 양재역);
        Path 경로 = Path.createPath(경로_역_목록, 여러노선_포함_구간, PathType.DISTANCE, 10, 20);

        assertThat(경로.getFare()).isEqualTo(2150);  // 기본 요금 1250 + 거리 추가 요금 0 + 신분당선 추가 요금 900 (가장 높은 추가 요금)
    }

    @Test
    @DisplayName("유효한 경로 확인")
    void isValidPath() {
        List<Station> 경로_역_목록 = Arrays.asList(강남역, 양재역);
        Path 경로 = Path.createPath(경로_역_목록, Arrays.asList(신분당선_강남_양재), PathType.DISTANCE, 8, 20);

        assertThat(경로.isValid()).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 경로 확인 - 빈 경로")
    void isInvalidPathEmpty() {
        List<Station> 빈_경로_역_목록 = Arrays.asList();
        Path 경로 = Path.createPath(빈_경로_역_목록, 신분당선_전체_구간, PathType.DISTANCE, 0, 20);

        assertThat(경로.isValid()).isFalse();
    }

    @Test
    @DisplayName("유효하지 않은 경로 확인 - 가중치 0")
    void isInvalidPathZeroWeight() {
        List<Station> 경로_역_목록 = Arrays.asList(강남역, 양재역);
        Path 경로 = Path.createPath(경로_역_목록, Arrays.asList(신분당선_강남_양재), PathType.DISTANCE, 0, 20);

        assertThat(경로.isValid()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 구간에 대한 예외 처리")
    void throwExceptionForNonExistentSection() {
        Station 잠실역 = new Station("잠실역");
        List<Station> 유효하지않은_경로_역_목록 = Arrays.asList(강남역, 잠실역);

        assertThatThrownBy(() -> Path.createPath(유효하지않은_경로_역_목록, 신분당선_전체_구간, PathType.DISTANCE, 10, 20))
                .isInstanceOf(SectionNotFoundException.class);
    }

    @Test
    @DisplayName("어린이 요금 계산 테스트")
    void calculateFareForChild() {
        List<Station> 경로_역_목록 = Arrays.asList(강남역, 양재역, 양재시민의숲역);
        Path 경로 = Path.createPath(경로_역_목록, 신분당선_전체_구간, PathType.DISTANCE, 11, 10);  // 10세 어린이

        // 기본 요금 1250 + 거리 추가 요금 100 + 신분당선 추가 요금 900 = 2250
        // (2250 - 350) * 0.5 = 950
        assertThat(경로.getFare()).isEqualTo(950);
    }

    @Test
    @DisplayName("청소년 요금 계산 테스트")
    void calculateFareForTeenager() {
        List<Station> 경로_역_목록 = Arrays.asList(강남역, 양재역, 양재시민의숲역);
        Path 경로 = Path.createPath(경로_역_목록, 신분당선_전체_구간, PathType.DISTANCE, 11, 15);  // 15세 청소년

        // 기본 요금 1250 + 거리 추가 요금 100 + 신분당선 추가 요금 900 = 2250
        // (2250 - 350) * 0.8 = 1520
        assertThat(경로.getFare()).isEqualTo(1520);
    }

    @Test
    @DisplayName("성인 요금 계산 테스트")
    void calculateFareForAdult() {
        List<Station> 경로_역_목록 = Arrays.asList(강남역, 양재역, 양재시민의숲역);
        Path 경로 = Path.createPath(경로_역_목록, 신분당선_전체_구간, PathType.DISTANCE, 11, 25);  // 25세 성인

        // 기본 요금 1250 + 거리 추가 요금 100 + 신분당선 추가 요금 900 = 2250
        assertThat(경로.getFare()).isEqualTo(2250);
    }
}
