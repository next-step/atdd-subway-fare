package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.AcceptanceContext;
import nextstep.exception.ExceptionResponse;
import nextstep.subway.domain.entity.PathSearchType;
import nextstep.subway.domain.response.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static nextstep.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
        Given("{string}과 {string}의 경로를 조회하면", (String source, String target) -> {
            context.response = 지하철_경로_조회(source, target, PathSearchType.DISTANCE);
        });
        Given("예외 - {string}과 {string}의 경로를 조회하면", (String source, String target) -> {
            context.message = 지하철_경로_조회(source, target, PathSearchType.DISTANCE)
                    .as(ExceptionResponse.class).getMessage();
        });
        Then("{string} 경로가 조회된다", (String pathString) -> {
            List<String> split = List.of(pathString.split(","));
            assertThat(context.response.jsonPath().getList("stationList.name", String.class)).containsExactly(split.toArray(new String[0]));
        });
        Then("출발역과 도착역이 같아 경로 조회를 할 수 없다", () -> {
            assertThat(context.message).isEqualTo(SAME_SOURCE_TARGET_EXCEPTION.getMessage());
        });
        Then("출발역과 도착역이 연결되어 있지 않아 경로 조회를 할 수 없다", () -> {
            assertThat(context.message).isEqualTo(NOT_CONNECTED_EXCEPTION.getMessage());
        });
        Then("노선에 존재하지 않는 역은 경로 조회를 할 수 없다", () -> {
            assertThat(context.message).isEqualTo(NO_EXISTS_STATION_EXCEPTION.getMessage());
        });
        Given("{string}에서 {string}까지의 최소 시간 기준으로 경로 조회를 요청하면", (String source, String target) -> {
            context.response = 지하철_경로_조회(source, target, PathSearchType.DURATION);
        });
        Then("최소 시간 기준 경로를 응답한다 - {string}", (String pathString) -> {
            List<String> split = List.of(pathString.split(","));
            assertThat(context.response.jsonPath().getList("stationList.name", String.class)).containsExactly(split.toArray(new String[0]));
        });
        Then("총 거리와 소요 시간을 함께 응답한다", () -> {
            assertThat(context.response.jsonPath().getInt("distance")).isEqualTo(20);
            assertThat(context.response.jsonPath().getInt("duration")).isEqualTo(16);
        });
    }

    private ExtractableResponse<Response> 지하철_경로_조회(String source, String target, PathSearchType type) {
        Long sourceId = ((StationResponse) context.store.get(source)).getId();
        Long targetId = ((StationResponse) context.store.get(target)).getId();
        return RestAssured.given().log().all()
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", sourceId, targetId, type)
                .then().log().all()
                .extract();
    }
}
