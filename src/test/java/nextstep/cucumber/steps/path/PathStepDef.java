package nextstep.cucumber.steps.path;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.steps.auth.AuthSteps;
import nextstep.cucumber.steps.member.MemberSteps;
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

    String accessToken;

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
                int extraFare = Integer.parseInt(row.get("extraFare"));
                String firstDepartureTime = row.get("firstDepartureTime");
                String lastDepartureTime = row.get("lastDepartureTime");
                int intervalTime = Integer.parseInt(row.get("intervalTime"));

                isFirstSectionForLine.putIfAbsent(line, true);
                if (Boolean.TRUE.equals(isFirstSectionForLine.get(line))) {
                    LineSteps.노선_생성_요청2(line, stations.get(upstation), stations.get(downstation), distance, duration, extraFare,
                                            firstDepartureTime, lastDepartureTime, intervalTime);
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

        And("최단 경로에 대한 요금은 {int}원이어야 한다", (Integer expectedFare) -> {
            int fare = PathSteps.parseFare(response);
            assertThat(fare).isEqualTo(expectedFare);
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

        And("최소 시간 경로에 대한 요금은 {int}원이어야 한다", (Integer expectedFare) -> {
            int fare = PathSteps.parseFare(response);
            assertThat(fare).isEqualTo(expectedFare);
        });

        Given("로그인한 사용자는 어린이의 나이로 설정되어 있다", () -> {
            MemberSteps.회원_생성_요청("test@test.com", "password", 7);
            accessToken = AuthSteps.로그인_요청("test@test.com", "password");
        });

        When("로그인한 어린이 사용자가 {string}에서 {string}까지의 최단 경로를 조회하면", (String source, String target) -> {
            response = PathSteps.경로_조회_요청_with_로그인(stations.get(source), stations.get(target), "DISTANCE", accessToken);
        });

        Then("어린이 사용자의 요금은 {int}원이어야 한다", (Integer expectedFare) -> {
            int fare = PathSteps.parseFare(response);
            assertThat(fare).isEqualTo(expectedFare);
        });

        Given("로그인한 사용자는 청소년의 나이로 설정되어 있다", () -> {
            MemberSteps.회원_생성_요청("test@test.com", "password", 13);
            accessToken = AuthSteps.로그인_요청("test@test.com", "password");
        });

        When("로그인한 청소년 사용자가 {string}에서 {string}까지의 최단 경로를 조회하면", (String source, String target) -> {
            response = PathSteps.경로_조회_요청_with_로그인(stations.get(source), stations.get(target), "DISTANCE", accessToken);
        });

        Then("청소년 사용자의 요금은 {int}원이어야 한다", (Integer expectedFare) -> {
            int fare = PathSteps.parseFare(response);
            assertThat(fare).isEqualTo(expectedFare);
        });

        When("사용자가 {string}에 {string}에서 {string}까지의 가장 빠른 도착 시간 경로를 조회하면", (String departureTime, String source, String target) -> {
            response = PathSteps.경로_조회_요청_with_출발시간(stations.get(source), stations.get(target), "ARRIVAL_TIME", departureTime);
        });

        Then("조회된 가장 빠른 도착 시간 경로는 다음과 같아야 한다", (DataTable table) -> {
            List<String> expectedStationNames = table.asList();
            List<String> stationNames = PathSteps.parseStationNames(response);
            assertThat(stationNames).containsExactlyElementsOf(expectedStationNames);
        });

        And("가장 빠른 도착 시간은 {string}이어야 한다", (String expectedArrivalTime) -> {
            String arrivalTime = PathSteps.parseArrivalTime(response);
            assertThat(arrivalTime).isEqualTo(expectedArrivalTime);
        });
    }
}