package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.RestAssured;
import nextstep.cucumber.AcceptanceContext;
import nextstep.path.PathSteps;
import nextstep.station.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {

    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {

        Given("{string}과 {string}의 최단 거리 경로를 조회하면", (String source, String target) -> getPath(source, target, "DISTANCE"));

        Given("{string}과 {string}의 최소 시간 경로를 조회하면", (String source, String target) -> getPath(source, target, "DURATION"));

        Then("{string} 경로가 조회된다", (String pathString) -> {
            List<String> split = List.of(pathString.split(","));
            assertThat(context.response.jsonPath().getList("stations.name", String.class)).containsExactly(split.toArray(new String[0]));
        });

        And("총 거리 {int}km와 소요 시간 {int}분을 응답한다", (Integer distance, Integer duration) -> {
            assertThat(context.response.jsonPath().getInt("duration")).isEqualTo(duration);
            assertThat(context.response.jsonPath().getInt("distance")).isEqualTo(distance);
        });

        And("지하철 이용 요금 {int}원을 응답한다", (Integer fare) -> {
            assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(fare);
        });
    }

    private void getPath(String source, String target, String type) {
        Long sourceId = ((StationResponse) context.store.get(source)).getId();
        Long targetId = ((StationResponse) context.store.get(target)).getId();
        context.response = PathSteps.getPath(sourceId, targetId, type);
    }
}