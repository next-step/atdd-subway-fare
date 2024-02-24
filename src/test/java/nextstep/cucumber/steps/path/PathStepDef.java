package nextstep.cucumber.steps.path;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.steps.station.StationSteps;
import nextstep.cucumber.steps.line.LineSteps;
import nextstep.cucumber.steps.line.SectionSteps;
import nextstep.utils.DatabaseCleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    @Autowired
    DatabaseCleanup databaseCleanup;
    ExtractableResponse<Response> response;

    Map<String, Long> stations = new HashMap<>();

    public PathStepDef() {
        Before(() -> {
            databaseCleanup.execute();
        });

        // Background 스텝 정의
        Given("\"교대역\", \"강남역\", \"양재역\", \"남부터미널역\" 지하철 역이 생성되어 있다", (DataTable table) -> {
            List<Map<String, String>> stationInput = table.asMaps();

            stationInput.forEach(row -> {
                String stationName = row.get("name");
                Long stationId = Long.parseLong(row.get("id"));
                stations.put(stationName, stationId);

                StationSteps.역_생성_요청(stationName);
            });
        });

        And("노선에 역이 연결되어 있다", (DataTable table) -> {
            List<Map<String, String>> lineSections = table.asMaps();
            Map<String, Boolean> isFirstSectionForLine = new HashMap<>();

            lineSections.forEach(row -> {
                String line = row.get("line");
                Long lineId = Long.parseLong(row.get("lineId"));
                String upstation = row.get("upstation");
                String downstation = row.get("downstation");
                int distance = Integer.parseInt(row.get("distance"));
                int duration = Integer.parseInt(row.get("duration"));

                isFirstSectionForLine.putIfAbsent(line, true);
                if (Boolean.TRUE.equals(isFirstSectionForLine.get(line))) {
                    LineSteps.노선_생성_요청(line, stations.get(upstation), stations.get(downstation), distance, duration);
                    isFirstSectionForLine.put(line, false);
                } else {
                    SectionSteps.구간_추가_요청(lineId, stations.get(upstation), stations.get(downstation), distance, duration);
                }
            });
        });

        When("사용자가 {string}에서 {string}까지의 최단 경로를 조회하면", (String source, String target) -> {

            response = PathSteps.경로_조회_요청(stations.get(source), stations.get(target), "DISTANCE");
        });

        Then("조회된 최단 경로는 다음과 같아야 한다", (DataTable table) -> {
            List<String> expectedStationNames = table.asList();
            List<String> stationNames = PathSteps.parseStationNames(response);
            assertThat(stationNames).containsExactlyElementsOf(expectedStationNames);
        });

        Then("최단 경로에 대한 총 거리는 {int}, 총 소요 시간은 {int}이어야 한다", (Integer expectedDistance, Integer expectedDuration) -> {
            int distance = PathSteps.parseDistance(response);
            int duration = PathSteps.parseDuration(response);
            assertThat(duration).isEqualTo(expectedDuration);
            assertThat(distance).isEqualTo(expectedDistance);
        });

        When("사용자가 출발지와 도착지 모두를 {string}으로 설정하여 최단 경로를 조회하면", (String name) -> {
            Long id = stations.get(name);
            response = PathSteps.경로_조회_요청(id, id, "DISTANCE");
        });

        Then("조회는 실패한다", () -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        });

        Then("\"출발역과 도착역이 동일합니다.\"라는 메시지가 표시된다", () -> {
            assertThat(response.body().asString()).contains("출발역과 도착역이 동일합니다.");
        });

        When("사용자가 {string}에서 {string}까지의 최소 시간 경로를 조회하면", (String source, String target) -> {
            response = PathSteps.경로_조회_요청(stations.get(source), stations.get(target), "DURATION");
        });

        Then("조회된 최소 시간 경로는 다음과 같아야 한다", (DataTable table) -> {
            List<String> expectedStationNames = table.asList();
            List<String> stationNames = PathSteps.parseStationNames(response);
            assertThat(stationNames).containsExactlyElementsOf(expectedStationNames);
        });

        Then("최소 시간 경로에 대한 총 거리는 {int}, 총 소요 시간은 {int}이어야 한다", (Integer expectedDistance, Integer expectedDuration) -> {
            int distance = PathSteps.parseDistance(response);
            int duration = PathSteps.parseDuration(response);
            assertThat(duration).isEqualTo(expectedDuration);
            assertThat(distance).isEqualTo(expectedDistance);
        });
    }
}