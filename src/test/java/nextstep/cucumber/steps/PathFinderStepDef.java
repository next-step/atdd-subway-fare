package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;
import nextstep.core.subway.station.fixture.StationFixture;
import nextstep.cucumber.util.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.core.subway.line.fixture.LineFixture.*;
import static nextstep.core.subway.line.step.LineSteps.지하철_노선_생성;
import static nextstep.core.subway.pathFinder.fixture.PathFinderFixture.*;
import static nextstep.core.subway.pathFinder.step.PathFinderSteps.*;
import static nextstep.core.subway.section.fixture.SectionFixture.지하철_구간;
import static nextstep.core.subway.section.step.SectionSteps.성공하는_지하철_구간_추가요청;
import static nextstep.core.subway.station.step.StationSteps.지하철_역_생성;

public class PathFinderStepDef implements En {

    @Autowired
    AcceptanceContext 컨텍스트;

    public PathFinderStepDef() {

        /**
         * 교대역    --- *2호선* ---   강남역
         * |                        |
         * *3호선*                   *신분당선*
         * |                        |
         * 남부터미널역  --- *3호선* ---   양재역
         * <p>
         * 오이도역 --- *4호선* --- 정왕역
         */
        Given("지하철 역과 노선을 생성하고, 구간을 추가한다.", () -> {
            컨텍스트.저장소_숫자_추가하기("교대역", 지하철_역_생성(StationFixture.교대역));
            컨텍스트.저장소_숫자_추가하기("강남역", 지하철_역_생성(StationFixture.강남역));
            컨텍스트.저장소_숫자_추가하기("양재역", 지하철_역_생성(StationFixture.양재역));
            컨텍스트.저장소_숫자_추가하기("남부터미널역", 지하철_역_생성(StationFixture.남부터미널역));
            컨텍스트.저장소_숫자_추가하기("정왕역", 지하철_역_생성(StationFixture.정왕역));
            컨텍스트.저장소_숫자_추가하기("오이도역", 지하철_역_생성(StationFixture.오이도역));
            컨텍스트.저장소_숫자_추가하기("존재하지_않는_역", 999L);

            컨텍스트.저장소_숫자_추가하기("이호선", 지하철_노선_생성(이호선(
                    컨텍스트.저장소_숫자_가져오기("교대역"), 컨텍스트.저장소_숫자_가져오기("강남역"), 10, 5)));
            컨텍스트.저장소_숫자_추가하기("신분당선", 지하철_노선_생성(신분당선(
                    컨텍스트.저장소_숫자_가져오기("강남역"), 컨텍스트.저장소_숫자_가져오기("양재역"), 10, 10)));
            컨텍스트.저장소_숫자_추가하기("삼호선", 지하철_노선_생성(삼호선(
                    컨텍스트.저장소_숫자_가져오기("교대역"), 컨텍스트.저장소_숫자_가져오기("남부터미널역"), 2, 7)));
            컨텍스트.저장소_숫자_추가하기("사호선", 지하철_노선_생성(사호선(
                    컨텍스트.저장소_숫자_가져오기("정왕역"), 컨텍스트.저장소_숫자_가져오기("오이도역"), 10, 5)));

            성공하는_지하철_구간_추가요청(
                    컨텍스트.저장소_숫자_가져오기("삼호선"),
                    지하철_구간(컨텍스트.저장소_숫자_가져오기("남부터미널역"), 컨텍스트.저장소_숫자_가져오기("양재역"), 3, 10));
        });

        When("{string}에서 {string}까지 최단거리 기준으로 경로를 조회할 경우", (String 출발역, String 도착역) -> {
            컨텍스트.저장된_응답 = 성공하는_지하철_경로_조회_요청(지하철_경로(
                    컨텍스트.저장소_숫자_가져오기(출발역),
                    컨텍스트.저장소_숫자_가져오기(도착역),
                    경로_조회_최단거리_타입));
        });

        When("출발역과 도착역이 동일한 경로를 조회할 경우", () -> {
            컨텍스트.저장소_정보_추가하기("조회할_구간", 지하철_경로(
                    컨텍스트.저장소_숫자_가져오기("강남역"),
                    컨텍스트.저장소_숫자_가져오기("강남역"),
                    경로_조회_최단거리_타입));
        });

        When("출발역과 도착역이 연결되어 있지 않은 경로를 조회할 경우", () -> {
            컨텍스트.저장소_정보_추가하기("조회할_구간", 지하철_경로(
                    컨텍스트.저장소_숫자_가져오기("강남역"),
                    컨텍스트.저장소_숫자_가져오기("오이도역"),
                    경로_조회_최단거리_타입));
        });

        When("출발역이 존재하지 않는 출발역으로 경로를 조회할 경우", () -> {
            컨텍스트.저장소_정보_추가하기("조회할_구간", 지하철_경로(
                    컨텍스트.저장소_숫자_가져오기("존재하지_않는_역"),
                    컨텍스트.저장소_숫자_가져오기("오이도역"),
                    경로_조회_최단거리_타입));
        });

        When("도착역이 존재하지 않는 도착역으로 경로를 조회할 경우", () -> {
            컨텍스트.저장소_정보_추가하기("조회할_구간", 지하철_경로(
                    컨텍스트.저장소_숫자_가져오기("오이도역"),
                    컨텍스트.저장소_숫자_가져오기("존재하지_않는_역"),
                    경로_조회_최단거리_타입));
        });

        Then("최단거리의 존재하는 역은 {string}, {string}, {string}이다.", (String first, String second, String third) -> {
            경로에_포함된_역_목록_검증(컨텍스트.저장된_응답,
                    컨텍스트.저장소_숫자_가져오기(first),
                    컨텍스트.저장소_숫자_가져오기(second),
                    컨텍스트.저장소_숫자_가져오기(third));
        });

        Then("최단거리는 {int}km 이다.", (Integer distance) -> {
            경로에_포함된_최단거리_검증(컨텍스트.저장된_응답, distance);
        });

        Then("소요 시간은 {int}분 이다.", (Integer duration) -> {
            경로에_포함된_소요_시간_검증(컨텍스트.저장된_응답, duration);
        });

        Then("최단거리의 존재하는 역 목록과 최단거리 값을 확인할 수 없다.", () -> {
            실패하는_지하철_경로_조회_요청((PathFinderRequest) 컨텍스트.저장소_정보_가져오기("조회할_구간"));
        });
    }
}
