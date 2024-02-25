package nextstep.cucumber.steps;

import static nextstep.fixture.SubwayScenarioFixtureCreator.*;
import static nextstep.utils.resthelper.ExtractableResponseParser.*;
import static nextstep.utils.resthelper.PathRequestExecutor.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Scenario;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/02/23
 */
public class PathStepDef implements En {

	private ExtractableResponse<Response> response;

	public PathStepDef() {
		Given("지하철역이 등록되어있음", this::createStations);

		And("지하철 노선이 등록되어있음", this::createLinesWithDuration);

		And("지하철 노선에 지하철역이 등록되어있음", this::createSectionsWithDuration);

		When("역ID {long}에서 역ID {long}까지의 최소 시간 경로를 조회하면", (Long sourceId, Long targetId) -> response = executeFindPathRequest(sourceId, targetId, "DURATION"));
		When("역ID {long}에서 역ID {long}까지의 최단 거리 경로를 조회하면", (Long sourceId, Long targetId) -> response = executeFindPathRequest(sourceId, targetId, "DISTANCE"));

		Then("최소 시간 기준 경로를 응답", this::verifyMinimumTimePath);
		Then("최단 거리 기준 경로를 응답", this::verifyMinimumDistancePath);
		And("총 거리와 소요 시간을 함께 응답함", this::verifyTotalDistanceWithTotalDuration);

	}


	private void verifyTotalDistanceWithTotalDuration(DataTable expectedPathTable) {
		List<Map<String, String>> expectedPath = expectedPathTable.asMaps(String.class, String.class);
		long expectedTotalDistance = Long.parseLong(expectedPath.get(0).get("distance"));
		long expectedTotalDuration = Long.parseLong(expectedPath.get(0).get("duration"));

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(parseDistance(response)).isEqualTo(expectedTotalDistance);
		assertThat(parseDuration(response)).isEqualTo(expectedTotalDuration);
	}

	private void verifyMinimumTimePath(DataTable expectedPathTable) {
		List<Map<String, String>> expectedPath = expectedPathTable.asMaps(String.class, String.class);
		List<String> expectedStationNames = Arrays.asList(expectedPath.get(0).get("stationNames").split(", "));

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(parseStations(response)).extracting("name").containsExactlyElementsOf(expectedStationNames);
	}

	private void verifyMinimumDistancePath(DataTable expectedPathTable) {
		List<Map<String, String>> expectedPath = expectedPathTable.asMaps(String.class, String.class);
		List<String> expectedStationNames = Arrays.asList(expectedPath.get(0).get("stationNames").split(", "));

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(parseStations(response)).extracting("name").containsExactlyElementsOf(expectedStationNames);
	}

	private void createLinesWithDuration(DataTable linesTable) {
		List<Map<String, String>> lines = linesTable.asMaps(String.class, String.class);
		lines.forEach(line -> {
			String name = line.get("line");
			long upStationId = Long.parseLong(line.get("upStationId"));
			long downStationId = Long.parseLong(line.get("downStationId"));
			long distance = Long.parseLong(line.get("distance"));
			int duration = Integer.parseInt(line.get("duration"));
			createLineWithDuration(name, upStationId, downStationId, distance, duration);
		});
	}

	private void createStations(DataTable stationsTable) {
		List<Map<String, String>> stations = stationsTable.asMaps(String.class, String.class);
		stations.forEach(station -> createStation(station.get("name")));
	}

	private void createSectionsWithDuration(DataTable sectionsTable) {
		List<Map<String, Long>> sections = sectionsTable.asMaps(String.class, Long.class);
		sections.forEach(section -> {
			Long lineId = section.get("lineId");
			Long upStationId = section.get("upStationId");
			Long downStationId = section.get("downStationId");
			Long distance = section.get("distance");
			int duration = Math.toIntExact(section.get("duration"));

			createSectionWithDuration(lineId, upStationId, downStationId, distance, duration);
		});
	}

}
