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
    AcceptanceContext context;

    public PathFinderStepDef() {
        Given("지하철 노선을 생성하고, 구간을 추가한다.", () -> {
            context.store.put("교대역", 지하철_역_생성(StationFixture.교대역));
            context.store.put("강남역", 지하철_역_생성(StationFixture.강남역));
            context.store.put("양재역", 지하철_역_생성(StationFixture.양재역));
            context.store.put("남부터미널역", 지하철_역_생성(StationFixture.남부터미널역));
            context.store.put("정왕역", 지하철_역_생성(StationFixture.정왕역));
            context.store.put("오이도역", 지하철_역_생성(StationFixture.오이도역));
            context.store.put("존재하지_않는_역", 999L);

            context.store.put("이호선", 지하철_노선_생성(이호선(
                    (Long) context.store.get("교대역"), (Long) context.store.get("강남역"), 10)));
            context.store.put("신분당선", 지하철_노선_생성(신분당선(
                    (Long) context.store.get("강남역"), (Long) context.store.get("양재역"), 10)));
            context.store.put("삼호선", 지하철_노선_생성(삼호선(
                    (Long) context.store.get("교대역"), (Long) context.store.get("남부터미널역"), 2)));
            context.store.put("사호선", 지하철_노선_생성(사호선(
                    (Long) context.store.get("정왕역"), (Long) context.store.get("오이도역"), 10)));

            성공하는_지하철_구간_추가요청(
                    (Long) context.store.get("삼호선"),
                    지하철_구간((Long) context.store.get("남부터미널역"), (Long) context.store.get("양재역"), 3));
        });

        When("출발역과 도착역을 통해 경로를 조회할 경우",  () -> {
            context.response = 성공하는_지하철_경로_조회_요청(지하철_경로(
                    (Long) context.store.get("강남역"),
                    (Long) context.store.get("남부터미널역")));
        });

        When("출발역과 도착역이 동일한 경로를 조회할 경우",  () -> {
            context.store.put("조회할_구간", new PathFinderRequest(
                    (Long) context.store.get("강남역"),
                    (Long) context.store.get("강남역")));
        });

        When("출발역과 도착역이 연결되어 있지 않은 경로를 조회할 경우",  () -> {
            context.store.put("조회할_구간", new PathFinderRequest(
                    (Long) context.store.get("강남역"),
                    (Long) context.store.get("오이도역")));
        });

        When("출발역이 존재하지 않는 출발역으로 경로를 조회할 경우",  () -> {
            context.store.put("조회할_구간", new PathFinderRequest(
                    (Long) context.store.get("존재하지_않는_역"),
                    (Long) context.store.get("오이도역")));
        });

        When("도착역이 존재하지 않는 도착역으로 경로를 조회할 경우",  () -> {
            context.store.put("조회할_구간", new PathFinderRequest(
                    (Long) context.store.get("오이도역"),
                    (Long) context.store.get("존재하지_않는_역")));
        });

        Then("최단거리의 존재하는 역 목록과 최단거리 값을 확인할 수 있다.", () -> {
            경로에_포함된_역_목록_검증(context.response,
                    (Long) context.store.get("강남역"),
                    (Long) context.store.get("교대역"),
                    (Long) context.store.get("남부터미널역"));

            경로에_포함된_최단거리_검증(context.response, 12);
        });

        Then("최단거리의 존재하는 역 목록과 최단거리 값을 확인할 수 없다.", () -> {
            실패하는_지하철_경로_조회_요청((PathFinderRequest) context.store.get("조회할_구간"));
        });
    }
}
