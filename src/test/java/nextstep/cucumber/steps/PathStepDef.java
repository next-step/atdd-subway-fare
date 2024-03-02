package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import nextstep.cucumber.AcceptanceContext;
import nextstep.exception.ExceptionResponse;
import nextstep.subway.LineSteps;
import nextstep.subway.PathSteps;
import nextstep.subway.StationSteps;
import nextstep.subway.controller.dto.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

public class PathStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
        Given("지하철역들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> rows = table.asMaps();

            for (Map<String, String> columns : rows) {
                String stationName = columns.get("name");

                Long id = StationSteps.지하철_요청을_구성한다()
                        .Response_HTTP_상태_코드(CREATED.value())
                        .로그인을_한다((String) context.store.get("adminAccessToken"))
                        .지하철_생성_정보를_설정한다(stationName)
                        .지하철_생성_요청을_보낸다()
                        .as(StationResponse.class).getId();
                context.store.put(stationName, id);
            }
        });

        And("지하철 노선들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> rows = table.asMaps();

            for (Map<String, String> columns : rows) {
                LineCreateRequest lineCreateRequest = new LineCreateRequest(
                        columns.get("name"),
                        columns.get("color"),
                        (long) context.store.get(columns.get("upStation")),
                        (long) context.store.get(columns.get("downStation")),
                        Long.parseLong(columns.get("distance")),
                        Long.parseLong(columns.get("duration")),
                        Long.parseLong(columns.get("extraFare"))
                );

                Long lineId = LineSteps.노선_요청을_구성한다()
                        .Response_HTTP_상태_코드(CREATED.value())
                        .로그인을_한다((String) context.store.get("adminAccessToken"))
                        .노선_생성_정보를_설정한다(lineCreateRequest)
                        .노선_생성_요청을_보낸다()
                        .as(LineResponse.class).getId();
                context.store.put(columns.get("name"), lineId);
            }
        });

        When("경로 조회시 출발역과 도착역이 같은 경우", () -> {
            String 강남역_ID = String.valueOf(context.store.get("강남역"));
            Map<String, String> params = Map.of("source", 강남역_ID, "target", 강남역_ID, "type", "DISTANCE");

            context.message = PathSteps.경로_요청을_구성한다()
                    .Response_HTTP_상태_코드(OK.value())
                    .로그인을_한다((String) context.store.get("adultAccessToken"))
                    .경로_조회_요청을_보낸다(params)
                    .as(ExceptionResponse.class).getMessage();
        });

        Then("출발역과 도착역이 같아 경로 조회를 할 수 없다", () -> {
            assertThat(context.message).isEqualTo("출발역과 도착역이 같은 경우 경로를 조회할 수 없습니다.");
        });

        When("경로 조회시 출발역과 도착역이 연결되어 있지 않은 경우", () -> {
            String 강남역_ID = String.valueOf(context.store.get("강남역"));
            String 신대방역_ID = String.valueOf(context.store.get("신대방역"));
            Map<String, String> params = Map.of("source", 강남역_ID, "target", 신대방역_ID, "type", "DISTANCE");

            context.message = PathSteps.경로_요청을_구성한다()
                    .Response_HTTP_상태_코드(OK.value())
                    .로그인을_한다((String) context.store.get("adultAccessToken"))
                    .경로_조회_요청을_보낸다(params)
                    .as(ExceptionResponse.class).getMessage();
        });

        Then("출발역과 도착역이 연결되어 있지 않아 경로 조회를 할 수 없다", () -> {
            assertThat(context.message).isEqualTo("출발역과 도착역이 연결되어 있지 않습니다.");
        });

        When("경로 조회시 존재하지 않는 출발역일 경우", () -> {
            String 강남역_ID = String.valueOf(context.store.get("강남역"));
            String 봉천역_ID = String.valueOf(context.store.get("봉천역"));
            Map<String, String> params = Map.of("source", 강남역_ID, "target", 봉천역_ID, "type", "DISTANCE");

            context.message = PathSteps.경로_요청을_구성한다()
                    .Response_HTTP_상태_코드(OK.value())
                    .로그인을_한다((String) context.store.get("adultAccessToken"))
                    .경로_조회_요청을_보낸다(params)
                    .as(ExceptionResponse.class).getMessage();
        });

        Then("노선에 존재하지 않는 출발역이여서 경로 조회를 할 수 없다", () -> {
            assertThat(context.message).isEqualTo("노선에 존재하지 않는 지하철역입니다.");
        });

        When("경로 조회시 존재하지 않는 도착역일 경우", () -> {
            String 강남역_ID = String.valueOf(context.store.get("강남역"));
            String 봉천역_ID = String.valueOf(context.store.get("봉천역"));
            Map<String, String> params = Map.of("source", 강남역_ID, "target", 봉천역_ID, "type", "DISTANCE");

            context.message = PathSteps.경로_요청을_구성한다()
                    .Response_HTTP_상태_코드(OK.value())
                    .로그인을_한다((String) context.store.get("adultAccessToken"))
                    .경로_조회_요청을_보낸다(params)
                    .as(ExceptionResponse.class).getMessage();
        });

        Then("노선에 존재하지 않는 도착역이여서 경로 조회를 할 수 없다", () -> {
            assertThat(context.message).isEqualTo("노선에 존재하지 않는 지하철역입니다.");
        });

        When("최소 거리와 기본 요금 및 어린이 할인 요금 테스트를 위한 강남역에서 선릉역까지 최소 시간 기준으로 경로 조회를 요청한다", () -> {
            String 강남역_ID = String.valueOf(context.store.get("강남역"));
            String 선릉역_ID = String.valueOf(context.store.get("선릉역"));
            Map<String, String> params = Map.of("source", 강남역_ID, "target", 선릉역_ID, "type", "DURATION");

            context.response = PathSteps.경로_요청을_구성한다()
                    .Response_HTTP_상태_코드(OK.value())
                    .로그인을_한다((String) context.store.get("childAccessToken"))
                    .경로_조회_요청을_보낸다(params);
        });

        Then("최소 거리와 기본 요금 및 어린이 할인 요금 테스트를 위한 최소 시간 기준 경로인 강남역, 선릉역을 응답한다", () -> {
            PathResponse pathResponse = context.response.as(PathResponse.class);
            assertAll(
                    () -> assertThat(pathResponse.getStations()).hasSize(2)
                            .extracting("id", "name")
                            .containsExactly(
                                    tuple(1L, "강남역"),
                                    tuple(2L, "선릉역")
                            )
            );
        });

        Then("최소 거리와 기본 요금 및 어린이 할인 요금 테스트 결과 최소 시간 기준 총 거리 10, 소요 시간 10를 함께 응답한다", () -> {
            assertDistanceAndDuration(10L, 10L);
        });

        Then("최소 시간 기준 결과 지하철 이용 요금인 1250원에서 추가 노선 요금인 100원을 더하고 어린이 할인 요금인 350원 공제 후 남은 금액의 20% 할인을 받아 900원을 함께 응답한다", () -> {
            assertFare(900L);
        });

        Given("최소 시간 경로 및 청소년 할인 요금 테스트를 위한 {string}을 생성한다", (String stationName) -> {
            StationCreateRequest request = new StationCreateRequest(stationName);
            Long id = StationSteps.지하철_요청을_구성한다()
                    .Response_HTTP_상태_코드(CREATED.value())
                    .로그인을_한다((String) context.store.get("adminAccessToken"))
                    .지하철_생성_정보를_설정한다(stationName)
                    .지하철_생성_요청을_보낸다()
                    .as(StationResponse.class).getId();
            context.store.put(stationName, id);
        });

        Given("최소 시간 경로 및 청소년 할인 요금 테스트를 위한 {string}과 {string}의 새로운 1호선 구간을 30D, 30T로 생성한다", (String sourceStationName, String targetStationName) -> {
            SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(
                    (long) context.store.get(("양재역")),
                    (long) context.store.get(("잠실역")),
                    30L,
                    30L
            );
            LineSteps.노선_요청을_구성한다()
                    .Response_HTTP_상태_코드(CREATED.value())
                    .로그인을_한다((String) context.store.get("adminAccessToken"))
                    .구간_생성_정보를_설정한다(sectionCreateRequest)
                    .구간_생성_요청을_보낸다(String.valueOf(context.store.get("일호선")));
        });

        Given("최소 시간 경로 및 청소년 할인 요금 테스트를 위한 {string}과 {string}의 새로운 신분당선 구간을 5D, 5T로 생성한다", (String sourceStationName, String targetStationName) -> {
            SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(
                    (long) context.store.get(("선릉역")),
                    (long) context.store.get(("잠실역")),
                    5L,
                    5L
            );
            LineSteps.노선_요청을_구성한다()
                    .Response_HTTP_상태_코드(CREATED.value())
                    .로그인을_한다((String) context.store.get("adminAccessToken"))
                    .구간_생성_정보를_설정한다(sectionCreateRequest)
                    .구간_생성_요청을_보낸다(String.valueOf(context.store.get("신분당선")));
        });

        When("최소 시간 경로 및 청소년 할인 요금 테스트를 위한 강남역에서 잠실역까지 최소 시간 기준으로 경로 조회를 요청한다", () -> {
            String 강남역_ID = String.valueOf(context.store.get("강남역"));
            String 잠실역_ID = String.valueOf(context.store.get("잠실역"));
            Map<String, String> params = Map.of("source", 강남역_ID, "target", 잠실역_ID, "type", "DURATION");

            context.response = PathSteps.경로_요청을_구성한다()
                    .Response_HTTP_상태_코드(OK.value())
                    .로그인을_한다((String) context.store.get("teenagerAccessToken"))
                    .경로_조회_요청을_보낸다(params);
        });

        Then("최소 시간 기준 경로인 강남역, 선릉역, 잠실역을 응답한다", () -> {
            PathResponse pathResponse = context.response.as(PathResponse.class);
            assertAll(
                    () -> assertThat(pathResponse.getStations()).hasSize(3)
                            .extracting("id", "name")
                            .containsExactly(
                                    tuple(1L, "강남역"),
                                    tuple(2L, "선릉역"),
                                    tuple(8L, "잠실역")
                            )
            );
        });

        Then("최소 시간 기준 총 거리 15, 소요 시간 15를 함께 응답한다", () -> {
            assertDistanceAndDuration(15L, 15L);
        });

        Then("최소 시간 기준 결과 지하철 이용 요금인 1350원에서 지하철 추가 요금인 100원을 추가하고 청소년 할인 요금인 350원 공제 후 남은 금액의 20% 할인을 받아 1150원을 함께 응답한다", () -> {
            assertFare(1250L);
        });

        Given("최소 거리 경로 테스트를 위한 {string}을 생성한다", (String stationName) -> {
            StationCreateRequest request = new StationCreateRequest(stationName);
            Long id = StationSteps.지하철_요청을_구성한다()
                    .Response_HTTP_상태_코드(CREATED.value())
                    .로그인을_한다((String) context.store.get("adminAccessToken"))
                    .지하철_생성_정보를_설정한다(stationName)
                    .지하철_생성_요청을_보낸다()
                    .as(StationResponse.class).getId();
            context.store.put(stationName, id);
        });

        Given("최소 거리 경로 테스트를 위한 {string}과 {string}의 새로운 1호선 구간을 30D, 30T로 생성한다", (String sourceStationName, String targetStationName) -> {
            SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(
                    (long) context.store.get(("양재역")),
                    (long) context.store.get(("잠실역")),
                    30L,
                    30L
            );

            LineSteps.노선_요청을_구성한다()
                    .Response_HTTP_상태_코드(CREATED.value())
                    .로그인을_한다((String) context.store.get("adminAccessToken"))
                    .구간_생성_정보를_설정한다(sectionCreateRequest)
                    .구간_생성_요청을_보낸다(String.valueOf(context.store.get("일호선")));
        });

        Given("최소 거리 경로 테스트를 위한 {string}과 {string}의 새로운 신분당선 구간을 100D, 100T로 생성한다", (String sourceStationName, String targetStationName) -> {
            SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(
                    (long) context.store.get(("선릉역")),
                    (long) context.store.get(("잠실역")),
                    100L,
                    100L
            );
            LineSteps.노선_요청을_구성한다()
                    .Response_HTTP_상태_코드(CREATED.value())
                    .로그인을_한다((String) context.store.get("adminAccessToken"))
                    .구간_생성_정보를_설정한다(sectionCreateRequest)
                    .구간_생성_요청을_보낸다(String.valueOf(context.store.get("신분당선")));
        });

        When("강남역에서 잠실역까지 최소 거리 기준으로 경로 조회를 요청한다", () -> {
            String 강남역_ID = String.valueOf(context.store.get("강남역"));
            String 잠실역_ID = String.valueOf(context.store.get("잠실역"));
            Map<String, String> params = Map.of("source", 강남역_ID, "target", 잠실역_ID, "type", "DISTANCE");

            context.response = PathSteps.경로_요청을_구성한다()
                    .Response_HTTP_상태_코드(OK.value())
                    .로그인을_한다((String) context.store.get("adultAccessToken"))
                    .경로_조회_요청을_보낸다(params);
        });

        Then("최소 거리 기준 경로인 강남역, 선릉역, 양재역, 잠실역을 응답한다", () -> {
            PathResponse pathResponse = context.response.as(PathResponse.class);
            assertAll(
                    () -> assertThat(pathResponse.getStations()).hasSize(4)
                            .extracting("id", "name")
                            .containsExactly(
                                    tuple(1L, "강남역"),
                                    tuple(2L, "선릉역"),
                                    tuple(3L, "양재역"),
                                    tuple(8L, "잠실역")
                            )
            );
        });

        Then("최소 거리 기준 총 거리 60, 소요 시간 60을 함께 응답한다", () -> {
            assertDistanceAndDuration(60L, 60L);
        });

        Then("최소 거리 기준 지하철 이용 요금인 2250원에서 가장 높은 금액의 노선 추가 요금인 1000원을 더해 3250원을 함께 응답한다", () -> {
            assertFare(3250L);
        });

        When("노선별 추가 요금 경로가 한개일 경우 강남역에서 선릉역까지 최소 거리 기준으로 경로 조회를 요청한다", () -> {
            String 강남역_ID = String.valueOf(context.store.get("강남역"));
            String 선릉역_ID = String.valueOf(context.store.get("선릉역"));
            Map<String, String> params = Map.of("source", 강남역_ID, "target", 선릉역_ID, "type", "DISTANCE");

            context.response = PathSteps.경로_요청을_구성한다()
                    .Response_HTTP_상태_코드(OK.value())
                    .로그인을_한다((String) context.store.get("adultAccessToken"))
                    .경로_조회_요청을_보낸다(params);
        });

        Then("노선별 추가 요금 경로가 한개일 경우 테스트를 위한 최소 거리 기준 경로인 강남역, 선릉역을 응답한다", () -> {
            PathResponse pathResponse = context.response.as(PathResponse.class);
            assertAll(
                    () -> assertThat(pathResponse.getStations()).hasSize(2)
                            .extracting("id", "name")
                            .containsExactly(
                                    tuple(1L, "강남역"),
                                    tuple(2L, "선릉역")
                            )
            );
        });

        Then("노선별 추가 요금 경로가 한개일 경우 테스트를 위한 최소 거리 기준 총 거리 10, 소요 시간 10을 함께 응답한다", () -> {
            assertDistanceAndDuration(10L, 10L);
        });

        Then("노선별 추가 요금 경로가 한개일 경우 테스트를 위한 최소 거리 기준 지하철 이용 요금인 1250원에서 추가 노선 요금인 100원을 더해 1350원을 함께 응답한다", () -> {
            assertFare(1350L);
        });

        When("노선별 추가 요금 경로가 두개일 경우 강남역에서 양재역까지 최소 거리 기준으로 경로 조회를 요청한다", () -> {
            String 강남역_ID = String.valueOf(context.store.get("강남역"));
            String 양재역_ID = String.valueOf(context.store.get("양재역"));
            Map<String, String> params = Map.of("source", 강남역_ID, "target", 양재역_ID, "type", "DISTANCE");

            context.response = PathSteps.경로_요청을_구성한다()
                    .Response_HTTP_상태_코드(OK.value())
                    .로그인을_한다((String) context.store.get("adultAccessToken"))
                    .경로_조회_요청을_보낸다(params);
        });

        Then("노선별 추가 요금 경로가 두개일 경우 테스트를 위한 최소 거리 기준 경로인 강남역, 선릉역, 양재역을 응답한다", () -> {
            PathResponse pathResponse = context.response.as(PathResponse.class);
            assertAll(
                    () -> assertThat(pathResponse.getStations()).hasSize(3)
                            .extracting("id", "name")
                            .containsExactly(
                                    tuple(1L, "강남역"),
                                    tuple(2L, "선릉역"),
                                    tuple(3L, "양재역")
                            )
            );
        });

        Then("노선별 추가 요금 경로가 두개일 경우 테스트를 위한 최소 거리 기준 총 거리 30, 소요 시간 30을 함께 응답한다", () -> {
            assertDistanceAndDuration(30L, 30L);
        });

        Then("노선별 추가 요금 경로가 두개일 경우 테스트를 위한 최소 거리 기준 지하철 이용 요금인 1650원에서 가장 높은 금액의 추가 노선 요금인 1000원을 더해 2650원을 함께 응답한다", () -> {
            assertFare(2650L);
        });
    }

    private void assertDistanceAndDuration(long distance, long duration) {
        PathResponse pathResponse = context.response.as(PathResponse.class);
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(distance),
                () -> assertThat(pathResponse.getDuration()).isEqualTo(duration)
        );
    }

    private void assertFare(long fare) {
        PathResponse pathResponse = context.response.as(PathResponse.class);
        assertAll(
                () -> assertThat(pathResponse.getFare()).isEqualTo(fare)
        );
    }
}

