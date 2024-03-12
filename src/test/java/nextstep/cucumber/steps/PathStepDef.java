package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.controller.dto.PathResponse;
import nextstep.subway.controller.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static nextstep.subway.acceptance.path.PathSteps.최단경로_조회요청;
import static nextstep.subway.acceptance.path.PathSteps.최소시간경로_조회요청;
import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
        Given("{string}과 {string}의 경로를 조회하면", (String source, String target) -> {
            Long sourceId = ((StationResponse) context.store.get(source)).getId();
            Long targetId = ((StationResponse) context.store.get(target)).getId();

            context.response = 최단경로_조회요청(sourceId, targetId);
        });

        Then("{string} 경로가 조회된다", (String pathString) -> {
            List<String> split = List.of(pathString.split(","));
            assertThat(context.response.jsonPath().getList("stations.name", String.class)).containsExactly(split.toArray(new String[0]));
        });

        When("{string}에서 {string}까지의 최소 시간 기준으로 경로 조회를 요청", (String source, String target) -> {
            Long sourceId = ((StationResponse) context.store.get(source)).getId();
            Long targetId = ((StationResponse) context.store.get(target)).getId();

            context.response = 최소시간경로_조회요청(sourceId, targetId);
        });

        Then("총 거리는 {string}이고 최소 시간은 {string}이다.", (String distance, String duration) -> {
            PathResponse pathResponse = context.response.as(PathResponse.class);

            assertThat(pathResponse.getDistance()).isEqualTo(Long.parseLong(distance));
            assertThat(pathResponse.getDuration()).isEqualTo(Long.parseLong(duration));
        });

        And("지하철 요금은 {int}원으로 조회된다.", (Integer fare) -> {
            PathResponse pathResponse = context.response.as(PathResponse.class);
            assertThat(pathResponse.getFare()).isEqualTo(fare);
        });
    }
}
