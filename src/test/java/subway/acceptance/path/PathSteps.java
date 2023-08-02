package subway.acceptance.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {
    public static ExtractableResponse<Response> getShortestPath(long sourceId, long targetId) {
        UriComponents retrieveQueryWithBaseUri = UriComponentsBuilder
                .fromUriString("/path")
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .build();
        return RestAssured.given().log().all()
                .when().get(retrieveQueryWithBaseUri.toUri())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getMinimumTimePath(long sourceId, long targetId) {
        UriComponents retrieveQueryWithBaseUri = UriComponentsBuilder
                .fromUriString("/path")
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .queryParam("type", "DURATION")
                .build();
        return RestAssured.given().log().all()
                .when().get(retrieveQueryWithBaseUri.toUri())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getShortestPathForDocument(long sourceId,
                                                                           long targetId,
                                                                           RequestSpecification spec,
                                                                           RestDocumentationFilter filter) {
        UriComponents retrieveQueryWithBaseUri = UriComponentsBuilder
                .fromUriString("/path")
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .queryParam("type", "DISTANCE")
                .build();
        return getPathForDocument(spec, filter, retrieveQueryWithBaseUri);
    }

    public static ExtractableResponse<Response> getMinimumTimePathForDocument(long sourceId,
                                                                              long targetId,
                                                                              RequestSpecification spec,
                                                                              RestDocumentationFilter filter) {
        UriComponents retrieveQueryWithBaseUri = UriComponentsBuilder
                .fromUriString("/path")
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .queryParam("type", "DURATION")
                .build();
        return getPathForDocument(spec, filter, retrieveQueryWithBaseUri);
    }

    private static ExtractableResponse<Response> getPathForDocument(RequestSpecification spec,
                                                                    RestDocumentationFilter filter,
                                                                    UriComponents uri) {
        return RestAssured.given(spec).log().all()
                .filter(filter)
                .when().get(uri.toUri())
                .then().log().all()
                .extract();
    }


    public static RestDocumentationFilter 최단거리경로_필터() {
        return document("shortestPath",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(parameterWithName("source").description("출발역 ID"),
                        parameterWithName("target").description("도착역 ID"),
                        parameterWithName("type").description("조회타입 : DISTANCE").optional()),
                responseFields(subsectionWithPath("stations").description("경로에 포함된 역 목록 (출발역-도착역 순)"),
                        subsectionWithPath("distance").description("경로의 총 구간 길이"),
                        subsectionWithPath("duration").description("경로의 총 소요 시간"),
                        subsectionWithPath("fare").description("경로의 운임")
                ));
    }

    public static RestDocumentationFilter 최소시간경로_필터() {
        return document("minimumTimePath",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(parameterWithName("source").description("출발역 ID"),
                        parameterWithName("target").description("도착역 ID"),
                        parameterWithName("type").description("조회타입 : DURATION")),
                responseFields(subsectionWithPath("stations").description("경로에 포함된 역 목록 (출발역-도착역 순)"),
                        subsectionWithPath("distance").description("경로의 총 구간 길이"),
                        subsectionWithPath("duration").description("경로의 총 소요 시간"),
                        subsectionWithPath("fare").description("경로의 운임")
                ));
    }
}
