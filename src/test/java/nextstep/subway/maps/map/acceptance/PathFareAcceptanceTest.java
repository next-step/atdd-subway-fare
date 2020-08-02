package nextstep.subway.maps.map.acceptance;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
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
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로와 요금 검색")
public class PathFareAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 역삼역;
    private Long 선릉역;
    private Long 한티역;
    private Long 도곡역;
    private Long 구룡역;
    private Long 남부터미널;
    private Long 양재역;
    private Long 매봉역;
    private Long 양재시민의숲;

    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> createLineResponse1 = 지하철_노선_등록되어_있음("2호선", "GREEN", 0);
        ExtractableResponse<Response> createLineResponse2 = 지하철_노선_등록되어_있음("3호선", "ORANGE", 500);
        ExtractableResponse<Response> createLineResponse3 = 지하철_노선_등록되어_있음("신분당선", "RED", 900);
        ExtractableResponse<Response> createLineResponse4 = 지하철_노선_등록되어_있음("분당선", "YELLOW", 300);

        final Long 이호선 = createLineResponse1.as(LineResponse.class).getId();
        final Long 삼호선 = createLineResponse2.as(LineResponse.class).getId();
        final Long 신분당 = createLineResponse3.as(LineResponse.class).getId();
        final Long 분당선 = createLineResponse4.as(LineResponse.class).getId();


        ExtractableResponse<Response> createdStationResponse1 = 지하철역_등록되어_있음("교대역");
        ExtractableResponse<Response> createdStationResponse2 = 지하철역_등록되어_있음("강남역");
        ExtractableResponse<Response> createdStationResponse3 = 지하철역_등록되어_있음("역삼역");
        ExtractableResponse<Response> createdStationResponse4 = 지하철역_등록되어_있음("선릉역");
        ExtractableResponse<Response> createdStationResponse5 = 지하철역_등록되어_있음("한티역");
        ExtractableResponse<Response> createdStationResponse6 = 지하철역_등록되어_있음("도곡역");
        ExtractableResponse<Response> createdStationResponse7 = 지하철역_등록되어_있음("구룡역");
        ExtractableResponse<Response> createdStationResponse8 = 지하철역_등록되어_있음("남부터미널");
        ExtractableResponse<Response> createdStationResponse9 = 지하철역_등록되어_있음("양재역");
        ExtractableResponse<Response> createdStationResponse10 = 지하철역_등록되어_있음("매봉역");
        ExtractableResponse<Response> createdStationResponse11 = 지하철역_등록되어_있음("양재시민의숲");

        교대역 = createdStationResponse1.as(StationResponse.class).getId();
        강남역 = createdStationResponse2.as(StationResponse.class).getId();
        역삼역 = createdStationResponse3.as(StationResponse.class).getId();
        선릉역 = createdStationResponse4.as(StationResponse.class).getId();
        한티역 = createdStationResponse5.as(StationResponse.class).getId();
        도곡역 = createdStationResponse6.as(StationResponse.class).getId();
        구룡역 = createdStationResponse7.as(StationResponse.class).getId();
        남부터미널 = createdStationResponse8.as(StationResponse.class).getId();
        양재역 = createdStationResponse9.as(StationResponse.class).getId();
        매봉역 = createdStationResponse10.as(StationResponse.class).getId();
        양재시민의숲 = createdStationResponse11.as(StationResponse.class).getId();

        // 2호선
        지하철_노선에_지하철역_등록되어_있음(이호선, null, 교대역, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(이호선, 교대역, 강남역, 2, 2);
        지하철_노선에_지하철역_등록되어_있음(이호선, 강남역, 역삼역, 2, 2);
        지하철_노선에_지하철역_등록되어_있음(이호선, 역삼역, 선릉역, 2, 2);

        // 3호선
        지하철_노선에_지하철역_등록되어_있음(삼호선, null, 교대역, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널, 1, 2);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 남부터미널, 양재역, 2, 2);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 양재역, 매봉역, 2, 2);

        // 신분당선
        지하철_노선에_지하철역_등록되어_있음(신분당, null, 강남역, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(신분당, 강남역, 양재역, 2, 2);
        지하철_노선에_지하철역_등록되어_있음(신분당, 양재역, 양재시민의숲, 10, 2);

        // 분당선
        지하철_노선에_지하철역_등록되어_있음(분당선, null, 선릉역, 0, 0);
        지하철_노선에_지하철역_등록되어_있음(분당선, 선릉역, 한티역, 2, 2);
        지하철_노선에_지하철역_등록되어_있음(분당선, 한티역, 도곡역, 2, 2);
        지하철_노선에_지하철역_등록되어_있음(분당선, 도곡역, 구룡역, 2, 2);
    }

    @DisplayName("추가 요금 900원이 있는 노선을 이용할 경우")
    @Test
    void extraFareFor8Km() {
        final ExtractableResponse<Response> response = 두_역의_경로를_조회한다(강남역, 양재역, "DURATION");

        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(2);
        assertThat(pathResponse.getDuration()).isEqualTo(2);
        assertThat(pathResponse.getFare()).isEqualTo(2150);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(강남역, 양재역));
    }

    @DisplayName("추가 요금 900원이 있는 노선을 이용할 경우 - 12km")
    @Test
    void extraFareFor12Km() {
        final ExtractableResponse<Response> response = 두_역의_경로를_조회한다(강남역, 양재시민의숲, "DURATION");

        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(2);
        assertThat(pathResponse.getDuration()).isEqualTo(2);
        assertThat(pathResponse.getFare()).isEqualTo(2250);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(강남역, 양재시민의숲));
    }

    @DisplayName("여러 노선 중 추가 요금이 가장 높은 금액만 추가되어 적용된다")
    @Test
    void extraFareForMultipleLines() {
        final ExtractableResponse<Response> response = 두_역의_경로를_조회한다(교대역, 구룡역, "DURATION");

        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(12);
        assertThat(pathResponse.getDuration()).isEqualTo(12);
        assertThat(pathResponse.getFare()).isEqualTo(1650);

        final List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(Lists.newArrayList(교대역, 강남역, 역삼역, 선릉역, 한티역, 도곡역, 구룡역));
    }

    @DisplayName("나는 청소년이다")
    @Test
    void fareForYouth() {

    }

    @DisplayName("나는 어린이다")
    @Test
    void fareForChildren() {

    }

    private ExtractableResponse<Response> 두_역의_경로를_조회한다(long src, long dst, String pathType) {
        return RestAssured.given().log().all().
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/paths?source={sourceId}&target={targetId}&type={type}", src, dst, pathType).
                then().
                log().all().
                extract();
    }
}
