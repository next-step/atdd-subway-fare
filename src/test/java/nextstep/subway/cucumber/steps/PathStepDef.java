package nextstep.subway.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.subway.cucumber.AcceptanceContext;
import nextstep.subway.path.application.dto.PathRequest;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.testhelper.apicaller.PathApiCaller;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
        Given("{string}과 {string}의 {string}경로를 조회하면", (String source, String target, String type) -> {
            Long sourceId = ((StationResponse) context.store.get(source)).getId();
            Long targetId = ((StationResponse) context.store.get(target)).getId();
            context.response = PathApiCaller.경로_조회(new PathRequest(sourceId, targetId, PathType.valueOf(type).toString()));
        });

        Then("{string} 경로가 조회된다", (String pathString) -> {
            List<String> split = List.of(pathString.split(","));
            assertThat(context.response.jsonPath().getList("stations.name", String.class)).containsExactly(split.toArray(new String[0]));
        });

        And("총 거리 {string}와 소요 시간 {string}을 함께 응답함", (String distance, String duration) -> {
            assertThat(context.response.jsonPath().getObject("distance", String.class)).isEqualTo(distance);
            assertThat(context.response.jsonPath().getObject("duration", String.class)).isEqualTo(duration);
        });

        And("지하철 이용 요금 {string}을 함께 응답함", (String fare) -> {
            assertThat(context.response.jsonPath().getObject("fare", String.class)).isEqualTo(fare);
        });
    }
}
