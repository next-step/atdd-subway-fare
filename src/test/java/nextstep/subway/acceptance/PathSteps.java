package nextstep.subway.acceptance;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.springframework.http.MediaType;

public class PathSteps {

    public static final String 최단거리 = "DISTANCE";
    public static final String 최소시간 = "DURATION";

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target) {
        return 타입_따라_두_역의_경로_조회를_요청(source, target, 최단거리, new RequestSpecBuilder().build());
    }

    public static ExtractableResponse<Response> 두_역의_최단_거리_경로_조회를_요청(Long source, Long target,
            RequestSpecification specification) {
        return 타입_따라_두_역의_경로_조회를_요청(source, target, 최단거리, specification);
    }

    public static ExtractableResponse<Response> 두_역의_최소_시간_경로_조회를_요청(Long source, Long target) {
        return 타입_따라_두_역의_경로_조회를_요청(source, target, 최소시간, new RequestSpecBuilder().build());
    }

    public static ExtractableResponse<Response> 타입_따라_두_역의_경로_조회를_요청(
            Long source,
            Long target,
            String type,
            RequestSpecification specification) {
        return RestAssured
                .given().log().all()
                .spec(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}&type={type}", source, target, type)
                .then().log().all().extract();
    }

    public static Long 지하철_노선_생성_요청(String name, String color, Long upStation, Long downStation, int distance,
            int duration) {
        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", name);
        lineCreateParams.put("color", color);
        lineCreateParams.put("upStationId", upStation + "");
        lineCreateParams.put("downStationId", downStation + "");
        lineCreateParams.put("distance", distance + "");
        lineCreateParams.put("duration", duration + "");

        return LineSteps.지하철_노선_생성_요청(lineCreateParams).jsonPath().getLong("id");
    }

    public static Map<String, String> 세션_생성_파라미터_생성(Long upStationId, Long downStationId, int distance, int duration) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId + "");
        params.put("downStationId", downStationId + "");
        params.put("distance", distance + "");
        params.put("duration", duration + "");
        return params;
    }

    public static PathResponse 경로_조회_예시_응답() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10
        );
        return pathResponse;
    }

    public static void 출력_필드_추가(String 출력_이름, RequestSpecification 요청_스펙) {
        요청_스펙.filter(document(출력_이름,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }
}
