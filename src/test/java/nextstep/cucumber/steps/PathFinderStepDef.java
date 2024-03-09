package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;
import nextstep.core.subway.station.fixture.StationFixture;
import nextstep.cucumber.util.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static nextstep.core.subway.line.fixture.LineFixture.*;
import static nextstep.core.subway.line.step.LineSteps.지하철_노선_생성;
import static nextstep.core.subway.pathFinder.fixture.PathFinderFixture.경로_조회_타입_찾기;
import static nextstep.core.subway.pathFinder.fixture.PathFinderFixture.지하철_경로;
import static nextstep.core.subway.pathFinder.step.PathFinderSteps.*;
import static nextstep.core.subway.section.fixture.SectionFixture.지하철_구간;
import static nextstep.core.subway.section.step.SectionSteps.성공하는_지하철_구간_추가요청;
import static nextstep.core.subway.station.step.StationSteps.지하철_역_생성;

public class PathFinderStepDef implements En {

    @Autowired
    AcceptanceContext 컨텍스트;

    public PathFinderStepDef() {

        /**
         * 교대역    --- *2호선*(10km, 5min) ---   강남역   ---(20km, 10min) ---  잠실역  --- *일호선(30km, 20min)* --- 신설동역
         * |                                       |                             |
         * *3호선* (2km, 7min)                  *신분당선*(10km, 3min)      *8호선*(5km, 5min)
         * |                                       |                             |
         * 남부터미널역  --- *3호선*(3km, 3min) ---양재역                          천호역
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
            컨텍스트.저장소_숫자_추가하기("잠실역", 지하철_역_생성(StationFixture.잠실역));
            컨텍스트.저장소_숫자_추가하기("천호역", 지하철_역_생성(StationFixture.천호역));
            컨텍스트.저장소_숫자_추가하기("신설동역", 지하철_역_생성(StationFixture.신설동역));
            컨텍스트.저장소_숫자_추가하기("존재하지_않는_역", 999L);

            컨텍스트.저장소_숫자_추가하기("이호선", 지하철_노선_생성(이호선(
                    컨텍스트.저장소_숫자_가져오기("교대역"), 컨텍스트.저장소_숫자_가져오기("강남역"), 10, 5)));
            컨텍스트.저장소_숫자_추가하기("신분당선", 지하철_노선_생성(신분당선(
                    컨텍스트.저장소_숫자_가져오기("강남역"), 컨텍스트.저장소_숫자_가져오기("양재역"), 10, 3)));
            컨텍스트.저장소_숫자_추가하기("삼호선", 지하철_노선_생성(삼호선(
                    컨텍스트.저장소_숫자_가져오기("교대역"), 컨텍스트.저장소_숫자_가져오기("남부터미널역"), 2, 7)));
            컨텍스트.저장소_숫자_추가하기("사호선", 지하철_노선_생성(사호선(
                    컨텍스트.저장소_숫자_가져오기("정왕역"), 컨텍스트.저장소_숫자_가져오기("오이도역"), 10, 5)));
            컨텍스트.저장소_숫자_추가하기("별내선", 지하철_노선_생성(별내선(
                    컨텍스트.저장소_숫자_가져오기("잠실역"), 컨텍스트.저장소_숫자_가져오기("천호역"), 5, 5)));
            컨텍스트.저장소_숫자_추가하기("일호선", 지하철_노선_생성(일호선(
                    컨텍스트.저장소_숫자_가져오기("잠실역"), 컨텍스트.저장소_숫자_가져오기("신설동역"), 30, 20)));

            성공하는_지하철_구간_추가요청(
                    컨텍스트.저장소_숫자_가져오기("삼호선"),
                    지하철_구간(컨텍스트.저장소_숫자_가져오기("남부터미널역"), 컨텍스트.저장소_숫자_가져오기("양재역"), 3, 3));
            성공하는_지하철_구간_추가요청(
                    컨텍스트.저장소_숫자_가져오기("이호선"),
                    지하철_구간(컨텍스트.저장소_숫자_가져오기("강남역"), 컨텍스트.저장소_숫자_가져오기("잠실역"), 20, 10));

        });

        When("{string}에서 {string}까지 {string} 기준으로 경로를 조회할 경우", (String 출발역, String 도착역, String 경로_조회_타입) -> {
            컨텍스트.저장된_응답 = 성공하는_지하철_경로_조회_요청(지하철_경로(
                    컨텍스트.저장소_숫자_가져오기(출발역),
                    컨텍스트.저장소_숫자_가져오기(도착역),
                    경로_조회_타입_찾기(경로_조회_타입)));
        });

        When("출발역과 도착역이 동일한 경로를 {string} 기준으로 조회할 경우", (String 경로_조회_타입) -> {
            컨텍스트.저장소_정보_추가하기("조회할_구간", 지하철_경로(
                    컨텍스트.저장소_숫자_가져오기("강남역"),
                    컨텍스트.저장소_숫자_가져오기("강남역"),
                    경로_조회_타입_찾기(경로_조회_타입)));
        });

        When("출발역과 도착역이 연결되어 있지 않은 경로를 {string} 기준으로 조회할 경우", (String 경로_조회_타입) -> {
            컨텍스트.저장소_정보_추가하기("조회할_구간", 지하철_경로(
                    컨텍스트.저장소_숫자_가져오기("강남역"),
                    컨텍스트.저장소_숫자_가져오기("오이도역"),
                    경로_조회_타입_찾기(경로_조회_타입)));
        });

        When("출발역이 존재하지 않는 경로를 {string} 기준으로 조회할 경우", (String 경로_조회_타입) -> {
            컨텍스트.저장소_정보_추가하기("조회할_구간", 지하철_경로(
                    컨텍스트.저장소_숫자_가져오기("존재하지_않는_역"),
                    컨텍스트.저장소_숫자_가져오기("오이도역"),
                    경로_조회_타입_찾기(경로_조회_타입)));
        });

        When("도착역이 존재하지 않는 경로를 {string} 기준으로 조회할 경우", (String 경로_조회_타입) -> {
            컨텍스트.저장소_정보_추가하기("조회할_구간", 지하철_경로(
                    컨텍스트.저장소_숫자_가져오기("오이도역"),
                    컨텍스트.저장소_숫자_가져오기("존재하지_않는_역"),
                    경로_조회_타입_찾기(경로_조회_타입)));
        });

        Then("경로 내 존재하는 역, 이동 거리, 소요 시간, 이용 요금을 확인할 수 있다.", (DataTable 데이터_테이블) -> {
            List<Map<String, String>> 행_목록 = 데이터_테이블.asMaps(String.class, String.class);
            행_목록.forEach(행 -> {
                String 경로_내_존재하는_역_목록 = 행.get("경로 내 존재하는 역 목록");

                List<Long> 역_번호_목록 = new ArrayList<>();
                Arrays.stream(경로_내_존재하는_역_목록.split(",")).forEach(역_이름 -> {
                    역_번호_목록.add(컨텍스트.저장소_숫자_가져오기(역_이름.trim()));
                });

                경로에_포함된_역_목록_검증(컨텍스트.저장된_응답, 역_번호_목록);
                경로에_포함된_최단거리_검증(컨텍스트.저장된_응답, Integer.parseInt(행.get("이동 거리")));
                경로에_포함된_소요_시간_검증(컨텍스트.저장된_응답, Integer.parseInt(행.get("소요 시간")));
                경로에_포함된_이용_요금_검증(컨텍스트.저장된_응답, Integer.parseInt(행.get("이용요금")));
            });
        });

        Then("최단거리의 존재하는 역은 {string}, {string}, {string}이다.", (String first, String second, String third) -> {
            경로에_포함된_역_목록_검증(컨텍스트.저장된_응답,
                    컨텍스트.저장소_숫자_가져오기(first),
                    컨텍스트.저장소_숫자_가져오기(second),
                    컨텍스트.저장소_숫자_가져오기(third));
        });

        Then("최단거리의 존재하는 역은 {string}, {string}, {string}, {string}이다.", (
                String first, String second,
                String third, String fourth) -> {
            경로에_포함된_역_목록_검증(컨텍스트.저장된_응답,
                    컨텍스트.저장소_숫자_가져오기(first),
                    컨텍스트.저장소_숫자_가져오기(second),
                    컨텍스트.저장소_숫자_가져오기(third),
                    컨텍스트.저장소_숫자_가져오기(fourth));
        });

        Then("최단거리는 {int}km 이다.", (Integer 예상하는_거리) -> {
            경로에_포함된_최단거리_검증(컨텍스트.저장된_응답, 예상하는_거리);
        });

        Then("소요 시간은 {int}분 이다.", (Integer 예상하는_시간) -> {
            경로에_포함된_소요_시간_검증(컨텍스트.저장된_응답, 예상하는_시간);
        });

        Then("이용 요금은 {int}원 이다.", (Integer 예상하는_금액) -> {
            경로에_포함된_이용_요금_검증(컨텍스트.저장된_응답, 예상하는_금액);
        });

        Then("경로를 조회할 수 없다.", () -> {
            실패하는_지하철_경로_조회_요청((PathFinderRequest) 컨텍스트.저장소_정보_가져오기("조회할_구간"));
        });
    }
}