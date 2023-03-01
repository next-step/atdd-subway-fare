package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {
    public static ExtractableResponse<Response> searchPath(RequestSpecification spec, long source, long target, String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", source);
        params.put("target", target);
        params.put("type", type);

        return RestAssured
                .given(spec).log().all()
                .filter(document("path", requestParameters(
                                parameterWithName("source").description("시작 지하철역 id"),
                                parameterWithName("target").description("종료 지하철역 id"),
                                parameterWithName("type").description("최단경로 검색 기준 타입")),
                        responseFields(
                                fieldWithPath("stations[]").type(JsonFieldType.ARRAY).description("결과코드"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("id"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("총거리"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("소요시간"),
                                fieldWithPath("totalCost").type(JsonFieldType.STRING).description("이용요금")
                        )))
                .params(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}
