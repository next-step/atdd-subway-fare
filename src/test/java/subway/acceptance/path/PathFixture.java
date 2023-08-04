package subway.acceptance.path;

import subway.acceptance.line.LineFixture;
import subway.acceptance.line.LineSteps;
import subway.acceptance.line.SectionFixture;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.station.domain.Station;

import java.util.List;

import static subway.acceptance.station.StationFixture.getStationId;

@SuppressWarnings("NonAsciiCharacters")
public class PathFixture {
    public static void 이호선_삼호선_신분당선_A호선_생성_호출() {
        var 이호선_요청 = LineFixture.generateLineCreateRequest("2호선", "bg-green-600", getStationId("강남역"), getStationId("교대역"), 10L, 5L);
        LineSteps.노선_생성_API(이호선_요청);

        var 삼호선_요청 = LineFixture.generateLineCreateRequest("3호선", "bg-amber-600", getStationId("교대역"), getStationId("남부터미널역"), 2L, 3L);
        var 삼호선_응답 = LineSteps.노선_생성_API(삼호선_요청);
        var 삼호선_URI = 삼호선_응답.header("Location");

        var 삼호선_끝에_구간_추가 = SectionFixture.구간_요청_만들기(getStationId("남부터미널역"), getStationId("양재역"), 3L, 15L);
        LineSteps.구간_추가_API(삼호선_URI, 삼호선_끝에_구간_추가);

        var 신분당선_요청 = LineFixture.generateLineCreateRequest("신분당선", "bg-hotpink-600", getStationId("강남역"), getStationId("양재역"), 10L, 5L);
        LineSteps.노선_생성_API(신분당선_요청);

        var A호선_요청 = LineFixture.generateLineCreateRequest("A호선", "bg-black-600", getStationId("건대역"), getStationId("성수역"), 7L, 1L);
        var A호선_응답 = LineSteps.노선_생성_API(A호선_요청);
        var A호선_URI = A호선_응답.header("Location");

        var A호선_끝에_구간_추가 = SectionFixture.구간_요청_만들기(getStationId("성수역"), getStationId("왕십리역"), 3L, 4L);
        LineSteps.구간_추가_API(A호선_URI, A호선_끝에_구간_추가);
        var A호선_끝에_구간_초과운임_추가 = SectionFixture.구간_요청_만들기(getStationId("왕십리역"), getStationId("강변역"), 17L, 8L);
        LineSteps.구간_추가_API(A호선_URI, A호선_끝에_구간_초과운임_추가);
        var A호선_끝에_구간_초과운임_50KM이상_추가 = SectionFixture.구간_요청_만들기(getStationId("강변역"), getStationId("잠실역"), 25L, 8L);
        LineSteps.구간_추가_API(A호선_URI, A호선_끝에_구간_초과운임_50KM이상_추가);
    }

    public static void 노선_요금을_가진_이호선_삼호선_신분당선_A호선_생성_호출() {
        var 이호선_요청 = LineFixture.generateLineCreateRequest("2호선", "bg-green-600", getStationId("강남역"), getStationId("교대역"), 10L, 5L,100L);
        LineSteps.노선_생성_API(이호선_요청);

        var 삼호선_요청 = LineFixture.generateLineCreateRequest("3호선", "bg-amber-600", getStationId("교대역"), getStationId("남부터미널역"), 2L, 3L, 500L);
        var 삼호선_응답 = LineSteps.노선_생성_API(삼호선_요청);
        var 삼호선_URI = 삼호선_응답.header("Location");

        var 삼호선_끝에_구간_추가 = SectionFixture.구간_요청_만들기(getStationId("남부터미널역"), getStationId("양재역"), 3L, 15L);
        LineSteps.구간_추가_API(삼호선_URI, 삼호선_끝에_구간_추가);

        var 신분당선_요청 = LineFixture.generateLineCreateRequest("신분당선", "bg-hotpink-600", getStationId("양재역"), getStationId("강남역"), 10L, 5L,1200L);
        LineSteps.노선_생성_API(신분당선_요청);

        var A호선_요청 = LineFixture.generateLineCreateRequest("A호선", "bg-black-600", getStationId("건대역"), getStationId("성수역"), 7L, 1L, 0L);
        var A호선_응답 = LineSteps.노선_생성_API(A호선_요청);
        var A호선_URI = A호선_응답.header("Location");

        var A호선_끝에_구간_추가 = SectionFixture.구간_요청_만들기(getStationId("성수역"), getStationId("왕십리역"), 3L, 4L);
        LineSteps.구간_추가_API(A호선_URI, A호선_끝에_구간_추가);
        var A호선_끝에_구간_초과운임_추가 = SectionFixture.구간_요청_만들기(getStationId("왕십리역"), getStationId("강변역"), 17L, 8L);
        LineSteps.구간_추가_API(A호선_URI, A호선_끝에_구간_초과운임_추가);
        var A호선_끝에_구간_초과운임_50KM이상_추가 = SectionFixture.구간_요청_만들기(getStationId("강변역"), getStationId("잠실역"), 25L, 8L);
        LineSteps.구간_추가_API(A호선_URI, A호선_끝에_구간_초과운임_50KM이상_추가);
    }

    public static List<Section> 단위_테스트_인스턴스_생성() {
        Station 강남역 = Station.builder().id(1L).name("강남역").build();
        Station 교대역 = Station.builder().id(2L).name("교대역").build();
        Station 남부터미널역 = Station.builder().id(3L).name("남부터미널역").build();
        Station 양재역 = Station.builder().id(4L).name("양재역").build();
        Station 건대역 = Station.builder().id(5L).name("건대역").build();
        Station 성수역 = Station.builder().id(6L).name("성수역").build();
        Station 왕십리역 = Station.builder().id(7L).name("왕십리역").build();
        Station 강변역 = Station.builder().id(8L).name("강변역").build();
        Station 잠실역 = Station.builder().id(9L).name("잠실역").build();

        Line 이호선 = Line.builder().id(1L).name("2호선").surcharge(100L).color("bg-green-600").build();
        Line 삼호선 = Line.builder().id(2L).name("3호선").surcharge(500L).color("bg-amber-600").build();
        Line 신분당선 = Line.builder().id(3L).name("신분당선").surcharge(1200L).color("bg-hotpink-600").build();
        Line A호선 = Line.builder().id(4L).name("A호선").surcharge(0L).color("bg-grey-600").build();

        Section 강남_교대_구간 = Section.builder().id(1L).line(이호선).distance(10L).duration(5L).upStation(교대역).downStation(강남역).build();
        Section 교대_남부터미널_구간 = Section.builder().id(2L).line(삼호선).distance(2L).duration(3L).upStation(교대역).downStation(남부터미널역).build();
        Section 남터_양재_구간 = Section.builder().id(3L).line(삼호선).distance(3L).duration(15L).upStation(남부터미널역).downStation(양재역).build();
        Section 양재_강남_구간 = Section.builder().id(4L).line(신분당선).distance(10L).duration(5L).upStation(양재역).downStation(강남역).build();
        Section 건대_성수_구간 = Section.builder().id(5L).line(A호선).distance(7L).duration(1L).upStation(건대역).downStation(성수역).build();
        Section 성수_왕십리_구간 = Section.builder().id(6L).line(A호선).distance(3L).duration(4L).upStation(성수역).downStation(왕십리역).build();
        Section 왕십리_강변_구간 = Section.builder().id(7L).line(A호선).distance(17L).duration(8L).upStation(왕십리역).downStation(강변역).build();
        Section 강변_잠실_구간 = Section.builder().id(8L).line(A호선).distance(25L).duration(8L).upStation(강변역).downStation(잠실역).build();

        이호선.addSection(강남_교대_구간);
        삼호선.addSection(교대_남부터미널_구간);
        삼호선.addSection(남터_양재_구간);
        신분당선.addSection(양재_강남_구간);
        A호선.addSection(건대_성수_구간);
        A호선.addSection(성수_왕십리_구간);
        A호선.addSection(왕십리_강변_구간);
        A호선.addSection(강변_잠실_구간);

        return List.of(강남_교대_구간, 교대_남부터미널_구간, 남터_양재_구간, 양재_강남_구간, 건대_성수_구간, 성수_왕십리_구간, 왕십리_강변_구간, 강변_잠실_구간);
    }

}
