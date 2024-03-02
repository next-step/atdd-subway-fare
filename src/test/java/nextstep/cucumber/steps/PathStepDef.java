package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.RestAssured;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.dto.path.PathResponse;
import nextstep.subway.dto.station.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
        Given("{string}과 {string}의 {string}경로를 조회하면", (String source, String target, String requestType) -> {
            Long sourceId = ((StationResponse) context.store.get(source)).getId();
            Long targetId = ((StationResponse) context.store.get(target)).getId();
            String type = requestType.equals("최단") ? "DISTANCE" : "DURATION";

            context.response = RestAssured.given().log().all()
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", sourceId, targetId, type)
                .then().log().all()
                .extract();
        });

        Then("최단 거리 기준 경로를 응답", () -> {
            assertThat(context.response.statusCode()).isEqualTo(HttpStatus.OK.value());
        });

        Then("최소 시간 기준 경로를 응답", () -> {
            assertThat(context.response.statusCode()).isEqualTo(HttpStatus.OK.value());
        });

        Then("{string} 경로가 조회된다", (String pathString) -> {
            List<String> split = List.of(pathString.split(","));
            assertThat(context.response.jsonPath().getList("stations.name", String.class)).containsExactly(split.toArray(new String[0]));
        });

        Then("최단거리 {string}가 조회된다", (String distance) -> {
            assertThat(context.response.as(PathResponse.class).getDistance()).isEqualTo(Integer.parseInt(distance));
        });

        Then("최소시간 {string}가 조회된다", (String duration) -> {
            assertThat(context.response.as(PathResponse.class).getDuration()).isEqualTo(Integer.parseInt(duration));
        });
    }
}
