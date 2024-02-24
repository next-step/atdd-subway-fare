package nextstep.subway.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.cucumber.AcceptanceContext;
import nextstep.subway.path.application.dto.PathRequest;
import nextstep.subway.path.application.dto.PathResponse;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.testhelper.JsonPathHelper;
import nextstep.subway.testhelper.apicaller.PathApiCaller;
import nextstep.subway.testhelper.fixture.LineFixture;
import nextstep.subway.testhelper.fixture.StationFixture;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
        Given("{string}과 {string}의 경로를 조회하면", (String source, String target) -> {
            Long sourceId = ((StationResponse) context.store.get(source)).getId();
            Long targetId = ((StationResponse) context.store.get(target)).getId();
            context.response = PathApiCaller.경로_조회(new PathRequest(sourceId, targetId));
        });

        Then("{string} 경로가 조회된다", (String pathString) -> {
            List<String> split = List.of(pathString.split(","));
            assertThat(context.response.jsonPath().getList("stations.name", String.class)).containsExactly(split.toArray(new String[0]));
        });

        And("총 거리 {string}와 소요 시간 {string}을 함께 응답함", (String distance, String duration) -> {
            assertThat(context.response.jsonPath().getObject("distance", String.class)).isEqualTo(distance);
            assertThat(context.response.jsonPath().getObject("duration", String.class)).isEqualTo(duration);
        });
    }
}
