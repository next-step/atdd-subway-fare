package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.RestAssured;
import nextstep.cucumber.AcceptanceContext;
import nextstep.exception.ExceptionResponse;
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
            Long sourceId = ((StationResponse) context.store.get(source)).getId();
            Long targetId = ((StationResponse) context.store.get(target)).getId();
            context.response = RestAssured.given().log().all()
                    .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                    .then().log().all()
                    .extract();
        });
        Given("예외 - {string}과 {string}의 경로를 조회하면", (String source, String target) -> {
            Long sourceId = ((StationResponse) context.store.get(source)).getId();
            Long targetId = ((StationResponse) context.store.get(target)).getId();
            context.message = RestAssured.given().log().all()
                    .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                    .then().log().all()
                    .extract().as(ExceptionResponse.class).getMessage();
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

    }
}
