package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.PathType;
import org.springframework.http.HttpStatus;

import static nextstep.auth.acceptance.AuthSteps.로그인_후_토큰_반환;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.line.LineUtils.responseToStationNames;
import static nextstep.subway.acceptance.line.LineUtils.지하철노선_생성_후_ID_반환;
import static nextstep.subway.acceptance.line.SectionUtils.지하철구간_생성;
import static nextstep.subway.acceptance.path.PathUtils.로그인후_지하철역_경로조회;
import static nextstep.subway.acceptance.path.PathUtils.지하철역_경로조회;
import static nextstep.subway.acceptance.station.StationUtils.지하철역_생성_후_id_추출;
import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {

    private final Map<String, Long> stationIds = new HashMap<>();
    private final Map<String, Long> lineIds = new HashMap<>();
    private final Map<String, String> memberTokens = new HashMap<>();

    private ExtractableResponse<Response> response;

    public PathStepDef() {
        Given("다음과 같은 지하철 역들이 존재한다", (io.cucumber.datatable.DataTable dataTable) -> {
            List<Map<String, String>> stations = dataTable.asMaps();
            for (Map<String, String> station : stations) {
                String name = station.get("역명");
                Long id = 지하철역_생성_후_id_추출(name);
                stationIds.put(name, id);
            }
        });

        Given("다음과 같은 지하철 노선들이 존재한다", (io.cucumber.datatable.DataTable dataTable) -> {
            List<Map<String, String>> lines = dataTable.asMaps();
            for (Map<String, String> line : lines) {
                String name = line.get("노선명");
                String color = line.get("색상");
                Long startId = stationIds.get(line.get("시작역"));
                Long endId = stationIds.get(line.get("종착역"));
                Long distance = Long.parseLong(line.get("거리"));
                Long duration = Long.parseLong(line.get("소요시간"));
                Long additionalFare = Long.parseLong(line.get("추가요금"));
                Long id = 지하철노선_생성_후_ID_반환(name, color, startId, endId, distance, duration, additionalFare);
                lineIds.put(name, id);
            }
        });

        Given("{string}에 새로운 구간이 등록되어 있다",
            (String lineName, io.cucumber.datatable.DataTable dataTable) -> {
                Map<String, String> section = dataTable.asMaps().get(0);
                Long lineId = lineIds.get(lineName);
                Long startId = stationIds.get(section.get("시작역"));
                Long endId = stationIds.get(section.get("종착역"));
                Long distance = Long.parseLong(section.get("거리"));
                Long duration = Long.parseLong(section.get("소요시간"));
                지하철구간_생성(lineId, startId, endId, distance, duration);
        });

        Given("{string}이 존재한다", (String stationName) -> {
            Long id = 지하철역_생성_후_id_추출(stationName);
            stationIds.put(stationName, id);
        });

        Given("사용자가 회원가입을 한다.", (DataTable dataTable) -> {
            List<Map<String, String>> users = dataTable.asMaps();
            for (Map<String, String> user : users) {
                String email = user.get("이메일");
                String password = user.get("비밀번호");
                int age = Integer.parseInt(user.get("나이"));
                회원_생성_요청(email, password, age);
            }
        });

        Given("사용자가 로그인을 한다.", (DataTable dataTable) -> {
            Map<String, String> user = dataTable.asMaps().get(0);
            String email = user.get("이메일");
            String password = user.get("비밀번호");
            String token = 로그인_후_토큰_반환(email, password);
            memberTokens.put(email, token);
        });

        When("사용자가 {string}에서 {string}까지의 경로를 조회하면", (String start, String end) -> {
            Long startId = stationIds.getOrDefault(start, 1L);
            Long endId = stationIds.getOrDefault(end, 3L);
            response = 지하철역_경로조회(startId, endId, PathType.DISTANCE);
        });

        When("사용자가 {string}에서 {string}까지의 최소 시간 기준으로 경로를 조회하면", (String start, String end) -> {
            Long startId = stationIds.getOrDefault(start, 1L);
            Long endId = stationIds.getOrDefault(end, 3L);
            response = 지하철역_경로조회(startId, endId, PathType.DURATION);
        });

        When("사용자 {string}가 {string}에서 {string}까지의 경로를 조회하면", (String email, String start, String end) -> {
            Long startId = stationIds.getOrDefault(start, 1L);
            Long endId = stationIds.getOrDefault(end, 3L);

            String accessToken = memberTokens.get(email);
            response = 로그인후_지하철역_경로조회(startId, endId, PathType.DISTANCE, accessToken);
        });

        Then("최단 경로가 다음과 같이 조회된다", (io.cucumber.datatable.DataTable dataTable) -> {
            List<Map<String, String>> rows = dataTable.asMaps();
            List<String> expectedStations = rows.stream()
                .map(row -> row.get("역명"))
                .collect(Collectors.toList());
            List<String> actualStations = responseToStationNames(response);
            assertThat(actualStations).containsExactlyElementsOf(expectedStations);
        });

        Then("예외가 발생한다", () -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        });

        Then("최소 시간 기준 경로를 응답한다", () -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            List<String> stationNames = response.jsonPath().getList("stations.name");
            assertThat(stationNames).isNotEmpty();
        });

        Then("최소 시간 경로가 다음과 같이 조회된다", (io.cucumber.datatable.DataTable dataTable) -> {
            List<Map<String, String>> rows = dataTable.asMaps();
            List<String> expectedStations = rows.stream()
                .map(row -> row.get("역명"))
                .collect(Collectors.toList());
            List<String> actualStations = responseToStationNames(response);
            assertThat(actualStations).containsExactlyElementsOf(expectedStations);
        });

        Then("총 거리와 소요 시간을 함께 응답한다", (DataTable dataTable) -> {
            Map<String, String> data = dataTable.asMaps().get(0);
            int expectedDistance = Integer.parseInt(data.get("총 거리"));
            int expectedDuration = Integer.parseInt(data.get("소요 시간"));

            assertThat(response.jsonPath().getInt("distance")).isEqualTo(expectedDistance);
            assertThat(response.jsonPath().getInt("duration")).isEqualTo(expectedDuration);
        });

        Then("지하철 이용 요금도 함께 응답한다", (DataTable dataTable) -> {
            Map<String, String> data = dataTable.asMaps().get(0);
            int expectedFare = Integer.parseInt(data.get("fare"));

            assertThat(response.jsonPath().getInt("fare")).isEqualTo(expectedFare);
        });
    }
}
