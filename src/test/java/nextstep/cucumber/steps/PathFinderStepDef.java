package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.core.subway.pathFinder.application.dto.PathFinderRequest;
import nextstep.core.subway.station.fixture.StationFixture;
import nextstep.cucumber.util.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.core.subway.line.fixture.LineFixture.*;
import static nextstep.core.subway.line.step.LineSteps.지하철_노선_생성;
import static nextstep.core.subway.pathFinder.fixture.PathFinderFixture.지하철_경로;
import static nextstep.core.subway.pathFinder.step.PathFinderSteps.*;
import static nextstep.core.subway.section.fixture.SectionFixture.지하철_구간;
import static nextstep.core.subway.section.step.SectionSteps.성공하는_지하철_구간_추가요청;
import static nextstep.core.subway.station.step.StationSteps.지하철_역_생성;

public class PathFinderStepDef implements En {

    @Autowired
    AcceptanceContext 컨텍스트;

    public PathFinderStepDef() {
        Given("지하철 노선을 생성하고, 구간을 추가한다.", () -> {
            컨텍스트.저장소_숫자_추가하기("교대역", 지하철_역_생성(StationFixture.교대역));
            컨텍스트.저장소_숫자_추가하기("강남역", 지하철_역_생성(StationFixture.강남역));
            컨텍스트.저장소_숫자_추가하기("양재역", 지하철_역_생성(StationFixture.양재역));
            컨텍스트.저장소_숫자_추가하기("남부터미널역", 지하철_역_생성(StationFixture.남부터미널역));
            컨텍스트.저장소_숫자_추가하기("정왕역", 지하철_역_생성(StationFixture.정왕역));
            컨텍스트.저장소_숫자_추가하기("오이도역", 지하철_역_생성(StationFixture.오이도역));
            컨텍스트.저장소_숫자_추가하기("존재하지_않는_역", 999L);

            컨텍스트.저장소_숫자_추가하기("이호선", 지하철_노선_생성(이호선(
                    컨텍스트.저장소_숫자_가져오기("교대역"), 컨텍스트.저장소_숫자_가져오기("강남역"), 10)));
            컨텍스트.저장소_숫자_추가하기("신분당선", 지하철_노선_생성(신분당선(
                    컨텍스트.저장소_숫자_가져오기("강남역"), 컨텍스트.저장소_숫자_가져오기("양재역"), 10)));
            컨텍스트.저장소_숫자_추가하기("삼호선", 지하철_노선_생성(삼호선(
                    컨텍스트.저장소_숫자_가져오기("교대역"), 컨텍스트.저장소_숫자_가져오기("남부터미널역"), 2)));
            컨텍스트.저장소_숫자_추가하기("사호선", 지하철_노선_생성(사호선(
                    컨텍스트.저장소_숫자_가져오기("정왕역"), 컨텍스트.저장소_숫자_가져오기("오이도역"), 10)));

            성공하는_지하철_구간_추가요청(
                    컨텍스트.저장소_숫자_가져오기("삼호선"),
                    지하철_구간(컨텍스트.저장소_숫자_가져오기("남부터미널역"), 컨텍스트.저장소_숫자_가져오기("양재역"), 3));
        });

        When("출발역과 도착역을 통해 경로를 조회할 경우", () -> {
            컨텍스트.저장된_응답 = 성공하는_지하철_경로_조회_요청(지하철_경로(
                    컨텍스트.저장소_숫자_가져오기("강남역"),
                    컨텍스트.저장소_숫자_가져오기("남부터미널역")));
        });

        When("출발역과 도착역이 동일한 경로를 조회할 경우", () -> {
            컨텍스트.저장소_정보_추가하기("조회할_구간", 지하철_경로(
                    컨텍스트.저장소_숫자_가져오기("강남역"),
                    컨텍스트.저장소_숫자_가져오기("강남역")));
        });

        When("출발역과 도착역이 연결되어 있지 않은 경로를 조회할 경우", () -> {
            컨텍스트.저장소_정보_추가하기("조회할_구간", 지하철_경로(
                    컨텍스트.저장소_숫자_가져오기("강남역"),
                    컨텍스트.저장소_숫자_가져오기("오이도역")));
        });

        When("출발역이 존재하지 않는 출발역으로 경로를 조회할 경우", () -> {
            컨텍스트.저장소_정보_추가하기("조회할_구간", 지하철_경로(
                    컨텍스트.저장소_숫자_가져오기("존재하지_않는_역"),
                    컨텍스트.저장소_숫자_가져오기("오이도역")));
        });

        When("도착역이 존재하지 않는 도착역으로 경로를 조회할 경우", () -> {
            컨텍스트.저장소_정보_추가하기("조회할_구간", 지하철_경로(
                    컨텍스트.저장소_숫자_가져오기("오이도역"),
                    컨텍스트.저장소_숫자_가져오기("존재하지_않는_역")));
        });

        Then("최단거리의 존재하는 역 목록과 최단거리 값을 확인할 수 있다.", () -> {
            경로에_포함된_역_목록_검증(컨텍스트.저장된_응답,
                    컨텍스트.저장소_숫자_가져오기("강남역"),
                    컨텍스트.저장소_숫자_가져오기("교대역"),
                    컨텍스트.저장소_숫자_가져오기("남부터미널역"));

            경로에_포함된_최단거리_검증(컨텍스트.저장된_응답, 12);
        });

        Then("최단거리의 존재하는 역 목록과 최단거리 값을 확인할 수 없다.", () -> {
            실패하는_지하철_경로_조회_요청((PathFinderRequest) 컨텍스트.저장소_정보_가져오기("조회할_구간"));
        });
    }
}
