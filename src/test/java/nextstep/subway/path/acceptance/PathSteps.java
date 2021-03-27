package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.Documentation;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {
    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int duration) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, duration);
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public static LineResponse 요금이_추가된_지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int duration, long addedCost) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, duration, addedCost);
        return 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 두_역의_최소_소요_시간_경로_조회를_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", "DURATION")
                .when().get("/paths")
                .then().log().all().extract();
    }

    public static void 경로_응답됨(ExtractableResponse<Response> response, List<Long> expectedStationIds, int distance, int duration) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
        assertThat(pathResponse.getDuration()).isEqualTo(duration);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }


    public static RequestParametersSnippet 지하철_노선_경로탐색_파라미터_설명() {
        return requestParameters(
                parameterWithName("source").description("경로 시작 역"),
                parameterWithName("target").description("경로 도착 역"),
                parameterWithName("type").description("경로 탐색 기준(거리 or 시간)")
        );
    }

    public static ResponseFieldsSnippet 지하철_노선_경로탐색_결과_필드_설명() {
         return responseFields(
                fieldWithPath("stations").description("조회된 경로내 지하철 역 리스트"),
                fieldWithPath("stations[].id").description("지하철 역 아이디"),
                fieldWithPath("stations[].name").description("지하철 역 이름"),
                fieldWithPath("stations[].createdDate").description("지하철 역 생성일"),
                fieldWithPath("stations[].modifiedDate").description("지하철 역 최종 변경일"),
                fieldWithPath("distance").description("경로 구간 길이"),
                fieldWithPath("duration").description("경로 구간 소요 시간"),
                fieldWithPath("cost").description("경로 구간 지불해야하는 금액")
        );
    }

    public static ExtractableResponse<Response> 두_역의_최단거리_탐색_요청(Documentation doc,TokenResponse tokenResponse, Long source, Long target) {
        return given(doc)
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all().extract();
    }

    private static RequestSpecification given(Documentation doc) {
        return doc.given(지하철_노선_경로탐색_파라미터_설명(), 지하철_노선_경로탐색_결과_필드_설명());
    }

    public static void 경로_요금_일치함(ExtractableResponse<Response> response, int fee) {
        PathResponse pathResponse = response.as(PathResponse.class);

        assertThat(pathResponse.getCost()).isEqualTo(fee);
    }

    public static ExtractableResponse<Response> 회원이_두_역의_최단_거리_경로_조회를_요청(TokenResponse tokenResponse, Long source, Long target) {
        return RestAssured.given().log().all().
                auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all().extract();
    }
}
