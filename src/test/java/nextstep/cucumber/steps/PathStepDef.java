package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.acceptance.line.LineApiRequester;
import nextstep.subway.acceptance.path.PathApiRequester;
import nextstep.subway.acceptance.section.SectionApiRequester;
import nextstep.subway.acceptance.station.StationApiRequester;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.SectionCreateRequest;
import nextstep.subway.path.PathResponse;
import nextstep.subway.path.PathType;
import nextstep.subway.station.dto.StationResponse;
import nextstep.utils.JsonPathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {

    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
        Given("{string}과 {string}의 {string} 경로를 조회하면", (String source, String target, String requestType) -> {
            Long sourceId = ((StationResponse) context.store.get(source)).getId();
            Long targetId = ((StationResponse) context.store.get(target)).getId();
            PathType type = requestType.equals("최단거리") ? PathType.DISTANCE : PathType.DURATION;

            context.response = given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/paths?source={sourceId}&target={targetId}&type={type}", sourceId, targetId, type)
                    .then().log().all()
                    .extract();
        });

        Then("{string} 경로가 조회된다", (String pathString) -> {
            List<String> stations = context.response.as(PathResponse.class).getStations().stream()
                    .map(StationResponse::getName)
                    .collect(Collectors.toList());

            assertThat(stations).containsExactly(pathString.split(","));
        });

        Then("총 거리 {int}km와 소요시간 {int}분을 함께 응답한다", (Integer distance, Integer duration) -> {
            PathResponse response = context.response.as(PathResponse.class);

            assertThat(response.getDistance()).isEqualTo(distance);
            assertThat(response.getDuration()).isEqualTo(duration);
        });
    }
}
