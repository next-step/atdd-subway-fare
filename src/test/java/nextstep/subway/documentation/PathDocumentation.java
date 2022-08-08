package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 10, 1250
        );

        when(pathService.findPath(anyLong(), anyLong(), any(), any())).thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(modifyUris()
                                        .scheme("https")
                                        .host("subway.nextstep.com")
                                        .removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역 id"),
                                parameterWithName("target").description("도착역 id"),
                                parameterWithName("type").description("경로조회 기준(DISTANCE, DURATION)")
                        ),
                        responseFields(
                                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로역"),
                                fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 id"),
                                fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역명"),
                                fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("역 생성일자"),
                                fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("역 수정일자"),
                                fieldWithPath("distance").type(JsonFieldType.NUMBER).description("총 거리"),
                                fieldWithPath("duration").type(JsonFieldType.NUMBER).description("총 소요시간"),
                                fieldWithPath("fare").type(JsonFieldType.NUMBER).description("요금정보")
                        )
                ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", PathType.DURATION)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
