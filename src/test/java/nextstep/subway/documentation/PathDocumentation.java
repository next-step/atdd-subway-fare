package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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
                        StationResponse.of(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        StationResponse.of(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 20
        );

        when(pathService.findPath(1L, 2L)).thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역 고유 번호"),
                                parameterWithName("target").description("도착역 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("stations[].id").description("지하철역 고유 번호").type(JsonFieldType.NUMBER),
                                fieldWithPath("stations[].name").description("지하철역 명").type(JsonFieldType.STRING),
                                fieldWithPath("stations[].createdDate").description("지하철역 생성 시간").type(JsonFieldType.STRING),
                                fieldWithPath("stations[].modifiedDate").description("마지막 지하철역 수정 시간").type(JsonFieldType.STRING),
                                fieldWithPath("distance").description("총 거리").type(JsonFieldType.NUMBER),
                                fieldWithPath("duration").description("총 소요 시간").type(JsonFieldType.NUMBER),
                                fieldWithPath("fare").description("총 운임 비용").type(JsonFieldType.NUMBER))
                ))
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .when().get("/paths")
                .then().log().all().extract();
    }
}
