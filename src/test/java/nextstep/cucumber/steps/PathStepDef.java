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
		Given("지하철 역들이 생성되어 있다:", this::createStations);

		And("지하철 노선들이 생성되어 있다:", this::createLines);

		And("다음 구간이 추가되어 있다:", this::createSections);

		When("역ID {long}에서 역ID {long}까지의 최단 경로를 조회하면", (Long sourceId, Long targetId) -> response = executeFindPathRequest(sourceId, targetId));

		Then("최단 경로가 정확하게 반환된다:", (DataTable expectedPathTable) -> verifyShortestPath(response, expectedPathTable));
	}

	private void createStations(DataTable stationsTable) {
		List<Map<String, String>> stations = stationsTable.asMaps(String.class, String.class);
		stations.forEach(station -> createStation(station.get("name")));
	}

	private void createLines(DataTable linesTable) {
		List<Map<String, String>> lines = linesTable.asMaps(String.class, String.class);
		lines.forEach(line -> {
			String name = line.get("line");
			long upStationId = Long.parseLong(line.get("upStationId"));
			long downStationId = Long.parseLong(line.get("downStationId"));
			long distance = Long.parseLong(line.get("distance"));
			createLine(name, upStationId, downStationId, distance);
		});
	}

	private void createSections(DataTable sectionsTable) {
		List<Map<String, Long>> sections = sectionsTable.asMaps(String.class, Long.class);
		sections.forEach(section -> {
			Long lineId = section.get("lineId");
			Long upStationId = section.get("upStationId");
			Long downStationId = section.get("downStationId");
			Long distance = section.get("distance");

			createSection(lineId, upStationId, downStationId, distance);
		});
	}

	private void verifyShortestPath(ExtractableResponse<Response> response, DataTable expectedPathTable) {
		List<Map<String, String>> expectedPath = expectedPathTable.asMaps(String.class, String.class);
		List<String> expectedStationNames = Arrays.asList(expectedPath.get(0).get("stationNames").split(", "));
		long expectedDistance = Long.parseLong(expectedPath.get(0).get("distance"));

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(parseStations(response)).extracting("name").containsExactlyElementsOf(expectedStationNames);
		assertThat(parseDistance(response)).isEqualTo(expectedDistance);
	}


}
