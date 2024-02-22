package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
            List<Map<String, String>> maps = table.asMaps();

            maps.forEach(row -> {
                String stationName = row.get("name");
                Long stationId = Long.parseLong(row.get("id"));
                stations.put(stationName, stationId);

                StationSteps.역_생성_요청(stationName);
            });
        });

        And("노선에 역이 연결되어 있다", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            Map<String, Boolean> isFirstSectionForLine = new HashMap<>();

            maps.forEach(row -> {
                String line = row.get("line");
                Long lineId = Long.parseLong(row.get("lineId"));
                String upstation = row.get("upstation");
                String downstation = row.get("downstation");
                int distance = Integer.parseInt(row.get("distance"));

                isFirstSectionForLine.putIfAbsent(line, true);
                if (Boolean.TRUE.equals(isFirstSectionForLine.get(line))) {
                    LineSteps.노선_생성_요청(line, stations.get(upstation), stations.get(downstation), distance);
                    isFirstSectionForLine.put(line, false);
                } else {
                    SectionSteps.구간_추가_요청(lineId, stations.get(upstation), stations.get(downstation), distance);
                }
            });
        });

        When("사용자가 {string}에서 {string}까지의 최단 경로를 조회하면", (String source, String target) -> {

            response = PathSteps.경로_조회_요청(stations.get(source), stations.get(target));
        });

        Then("조회된 경로는 다음과 같아야 한다", (DataTable table) -> {
            List<String> expectedStationNames = table.asList();
            List<String> stationNames = PathSteps.parseStationNames(response);
            assertThat(stationNames).containsExactlyElementsOf(expectedStationNames);
        });

        Then("총 거리는 5이어야 한다", () -> {
            int distance = PathSteps.parseDistance(response);
            assertThat(distance).isEqualTo(5);
        });

        When("사용자가 출발지와 도착지 모두를 {string}으로 설정하여 최단 경로를 조회하면", (String name) -> {
            Long id = stations.get(name);
            response = PathSteps.경로_조회_요청(id, id);
        });

        Then("조회는 실패한다", () -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        });

        Then("\"출발역과 도착역이 동일합니다.\"라는 메시지가 표시된다", () -> {
            assertThat(response.body().asString()).contains("출발역과 도착역이 동일합니다.");
        });
    }
}