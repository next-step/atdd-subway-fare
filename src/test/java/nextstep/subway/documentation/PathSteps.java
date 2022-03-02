package nextstep.subway.documentation;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathSteps {

    public static void 경로_조회_요청(RequestSpecification spec) {
        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        createRequestFieldsSnippet(),
                        createResponseFieldsSnippet()))
                .body(createParams())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/paths")
                .then().log().all().extract();
    }

    public static PathResponse createPathResponse() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 1, 1250
        );
        return pathResponse;
    }

    public static ResponseFieldsSnippet createResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("stations[].id").description("지하철역 아이디"),
                fieldWithPath("stations[].name").description("지하철역 이름"),
                fieldWithPath("stations[].createdDate").description("생성일"),
                fieldWithPath("stations[].modifiedDate").description("수정일"),
                fieldWithPath("distance").description("거리"),
                fieldWithPath("duration").description("소요시간"),
                fieldWithPath("fare").description("요금"));
    }

    public static RequestFieldsSnippet createRequestFieldsSnippet() {
        return requestFields(fieldWithPath("source").optional().description("출발역"),
                fieldWithPath("target").optional().description("도착역"),
                fieldWithPath("pathType").optional().description("경로 조회 타입"));
    }

    public static Map<String, Object> createParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("source", 1L);
        params.put("target", 2L);
        params.put("pathType", PathType.DISTANCE);
        return params;
    }

}
