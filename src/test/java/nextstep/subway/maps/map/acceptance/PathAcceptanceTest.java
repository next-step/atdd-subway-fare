package nextstep.subway.maps.map.acceptance;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.maps.line.dto.LineResponse;
import nextstep.subway.maps.map.dto.PathResponse;
import nextstep.subway.maps.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.maps.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.maps.line.acceptance.step.LineStationAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.maps.station.acceptance.step.StationAcceptanceStep.지하철역_등록되어_있음;
import static nextstep.subway.members.member.acceptance.step.MemberAcceptanceStep.로그인_되어_있음;
import static nextstep.subway.members.member.acceptance.step.MemberAcceptanceStep.회원_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 검색")
public class PathAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    private TokenResponse loginResponse;
    /**
     * 교대역      -      강남역
     * |                 |
     * 남부터미널역           |
     * |                 |
     * 양재역      -       -|
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("교대역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("양재역");
        ExtractableResponse<Response> createdStationResponse4 = 지하철역_등록되어_있음("남부터미널");
        ExtractableResponse<Response> createLineResponse1 = 지하철_노선_등록되어_있음("2호선", "GREEN", 200);
        ExtractableResponse<Response> createLineResponse2 = 지하철_노선_등록되어_있음("신분당선", "RED", 500);
        ExtractableResponse<Response> createLineResponse3 = 지하철_노선_등록되어_있음("3호선", "ORANGE", 900);

        Long lineId1 = createLineResponse1.as(LineResponse.class).getId();
        Long lineId2 = createLineResponse2.as(LineResponse.class).getId();
        Long lineId3 = createLineResponse3.as(LineResponse.class).getId();
        Long stationId1 = createdStationResponse1.as(StationResponse.class).getId();
        Long stationId2 = createdStationResponse2.as(StationResponse.class).getId();
        Long stationId3 = createdStationResponse3.as(StationResponse.class).getId();
        Long stationId4 = createdStationResponse4.as(StationResponse.class).getId();

        지하철_노선에_지하철역_등록되어_있음(lineId1, null, stationId1, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId1, stationId1, stationId2, 12, 2);

        지하철_노선에_지하철역_등록되어_있음(lineId2, null, stationId2, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId1, stationId2, stationId3, 12, 1);

        지하철_노선에_지하철역_등록되어_있음(lineId3, null, stationId1, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(lineId3, stationId1, stationId4, 1, 2);
        지하철_노선에_지하철역_등록되어_있음(lineId3, stationId4, stationId3, 12, 2);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        ExtractableResponse<Response> response = RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={sourceId}&target={targetId}&type={type}", 1L, 3L, "DISTANCE").
                then().
                log().all().
                extract();

        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(13);
        assertThat(pathResponse.getDuration()).isEqualTo(4);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, 4L, 3L));
    }

    @DisplayName("두 역의 최소 시간 경로를 조회한다.")
    @Test
    void findPathByDuration() {
        ExtractableResponse<Response> response = RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={sourceId}&target={targetId}&type={type}", 1L, 3L, "DURATION").
                then().
                log().all().
                extract();

        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(24);
        assertThat(pathResponse.getDuration()).isEqualTo(3);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(1L, 2L, 3L));
    }

    @DisplayName("두 역의 최소 시간 경로와 기본, 추가요금을 함께 조회한다.")
    @Test
    void findPathByDurationForExtraFare() {
        ExtractableResponse<Response> response = RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={sourceId}&target={targetId}&type={type}", 1L, 3L, "DURATION").
                then().
                log().all().
                extract();

        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(24);
        assertThat(pathResponse.getDuration()).isEqualTo(3);
        assertThat(pathResponse.getFare()).isEqualTo(1750);

        assertThat(pathResponse.getStations()).extracting(StationResponse::getId).containsExactly(1L, 2L, 3L);
    }

    @DisplayName("로그인한 어린이 사용자의 두 역의 최소 시간 경로와 기본, 추가요금을 함께 조회한다.")
    @Test
    void findPathByChild() {
        회원_등록되어_있음(EMAIL, PASSWORD, 8);
        loginResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = RestAssured.given().log().all().
                auth().oauth2(loginResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={sourceId}&target={targetId}&type={type}", 1L, 3L, "DURATION").
                then().
                log().all().
                extract();

        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(24);
        assertThat(pathResponse.getDuration()).isEqualTo(3);
        assertThat(pathResponse.getFare()).isEqualTo((1750 - 350) * 50 / 100);

        assertThat(pathResponse.getStations()).extracting(StationResponse::getId).containsExactly(1L, 2L, 3L);
    }

    @DisplayName("로그인한 청소년 사용자의 두 역의 최소 시간 경로와 기본, 추가요금을 함께 조회한다.")
    @Test
    void findPathByYouth() {
        회원_등록되어_있음(EMAIL, PASSWORD, 18);
        loginResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = RestAssured.given().log().all().
                auth().oauth2(loginResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={sourceId}&target={targetId}&type={type}", 1L, 3L, "DURATION").
                then().
                log().all().
                extract();

        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(24);
        assertThat(pathResponse.getDuration()).isEqualTo(3);
        assertThat(pathResponse.getFare()).isEqualTo((1750 - 350) * 80 / 100);

        assertThat(pathResponse.getStations()).extracting(StationResponse::getId).containsExactly(1L, 2L, 3L);
    }

}
