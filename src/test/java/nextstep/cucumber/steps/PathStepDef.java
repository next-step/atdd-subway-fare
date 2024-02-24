package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.common.api.PathApiHelper;
import nextstep.cucumber.AcceptanceContext;
import nextstep.path.domain.PathType;
import nextstep.station.application.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class PathStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
        Given("{string}과 {string}의 최단 거리 경로를 조회하면", (final String source, final String target) -> {
            final Long sourceId = context.store.get(source, StationResponse.class).getId();
            final Long targetId = context.store.get(target, StationResponse.class).getId();
            context.response = PathApiHelper.findPath(sourceId, targetId, PathType.DISTANCE);
        });
        Given("{string}과 {string}의 최단 시간 경로를 조회하면", (final String source, final String target) -> {
            final Long sourceId = context.store.get(source, StationResponse.class).getId();
            final Long targetId = context.store.get(target, StationResponse.class).getId();
            context.response = PathApiHelper.findPath(sourceId, targetId, PathType.DURATION);
        });
        Then("{string} 경로와 거리 {int}, 소요시간 {int}, 요금 {int}원이 조회된다",
                (final String pathString,
                 final Integer distance,
                 final Integer duration,
                 final Long fare
                ) -> {
                    final List<String> split = List.of(pathString.split(","));
                    assertSoftly(softly -> {
                        softly.assertThat(context.response.jsonPath().getList("stations.name", String.class)).containsExactly(split.toArray(new String[0]));
                        softly.assertThat(context.response.jsonPath().getInt("distance")).isEqualTo(distance);
                        softly.assertThat(context.response.jsonPath().getInt("duration")).isEqualTo(duration);
                        softly.assertThat(context.response.jsonPath().getLong("fare")).isEqualTo(fare);
                    });
                });
    }
}
