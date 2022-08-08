package nextstep.subway.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import java.time.LocalDateTime;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
            ), 10, 5
        );

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);
        RestAssured
            .given(spec).log().all()
            .filter(document("path",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("atdd-subway-fare")
                        .removePort(),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("source").description("출발역 ID"),
                    parameterWithName("target").description("도착역 ID"),
                    parameterWithName("pathType").description("조회 타입")
                ),
                responseFields(
                    fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로역"),
                    fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 ID"),
                    fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역명"),
                    fieldWithPath("distance").type(JsonFieldType.NUMBER).description("거리"),
                    fieldWithPath("duration").type(JsonFieldType.NUMBER).description("시간")
                )
            ))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 1L)
            .queryParam("target", 2L)
            .queryParam("pathType", PathType.DISTANCE.name())
            .when().get("/paths")
            .then().log().all().extract();
    }
}
