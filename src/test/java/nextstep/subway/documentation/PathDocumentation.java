package nextstep.subway.documentation;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 1
        );
        when(pathService.findPath(any())).thenReturn(pathResponse);

        Map<String, Object> params = new HashMap<>();
        params.put("source", 1L);
        params.put("target", 2L);
        params.put("pathType", PathType.DISTANCE);
        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(fieldWithPath("source").optional().description("출발역"),
                                        fieldWithPath("target").optional().description("도착역"),
                                        fieldWithPath("pathType").optional().description("경로 조회 타입")),
                        responseFields(
                                fieldWithPath("stations[].id").description("지하철역 아이디"),
                                fieldWithPath("stations[].name").description("지하철역 이름"),
                                fieldWithPath("stations[].createdDate").description("생성일"),
                                fieldWithPath("stations[].modifiedDate").description("수정일"),
                                fieldWithPath("distance").description("거리"),
                                fieldWithPath("duration").description("소요시간"))))
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/paths")
                .then().log().all().extract();
    }

}
