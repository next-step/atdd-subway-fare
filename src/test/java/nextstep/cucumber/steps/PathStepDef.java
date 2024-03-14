package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.application.dto.StationResponse;
import nextstep.subway.ui.controller.PathType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static nextstep.subway.utils.steps.PathSteps.최단_경로_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
	@Autowired
	private AcceptanceContext context;

	public PathStepDef() {
		When("{string}부터 {string}까지의 최단 {string} 경로를 조회하면", (String source, String target, String type) -> {
			context.response = 최단_경로_조회_요청(
					(((StationResponse) context.store.get(source)).getId()),
					(((StationResponse) context.store.get(target)).getId()),
					convertPathType(type)
			);
		});

		Then("경로에 있는 역 목록은 {string} 순서대로 구성된다", (String stationName) -> {
			String[] names = stationName.split(",");
			Long[] ids = Arrays.stream(names)
					.map(name -> ((StationResponse) context.store.get(name)).getId())
					.toArray(Long[]::new);

			assertThat(context.response.jsonPath().getList("stations.id", Long.class)).containsExactly(ids);
		});

		Then("경로의 소요 거리는 {string}이고 소요 시간은 {string}이다", (String distance, String duration) -> {
			assertThat(context.response.jsonPath().getInt("distance")).isEqualTo(Integer.parseInt(distance));
			assertThat(context.response.jsonPath().getInt("duration")).isEqualTo(Integer.parseInt(duration));
		});

		Then("이용 요금은 {string}원 이다", (String fare) -> {
			assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(Integer.parseInt(fare));
		});
	}

	private PathType convertPathType(String type) {
		if("거리".equals(type)) {
			return PathType.DISTANCE;
		}

		if("시간".equals(type)) {
			return PathType.DURATION;
		}

		return null;
	}
}
