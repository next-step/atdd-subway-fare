package nextstep.subway.documentation;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역"),
                        new StationResponse(2L, "역삼역")
                ), 10, 4, 1250
        );

        when(pathService.findPath(anyLong(), anyLong(), anyString())).thenReturn(pathResponse);

        RestAssured
                .given(spec).log().all()
                .filter(document("path",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("source").description("출발역 ID"),
                                        parameterWithName("target").description("도착역 ID"),
                                        parameterWithName("type").description("조회 방식(최단 거리 : DISTANCE, 최소 소요 시간: DURATION)")
                                ),
                                responseFields(
                                        fieldWithPath("stations[].id").description("지하철역 ID"),
                                        fieldWithPath("stations[].name").description("지하철역 이름"),
                                        fieldWithPath("distance").description("총 이동거리"),
                                        fieldWithPath("duration").description("총 소요 시간"),
                                        fieldWithPath("fare")
                                                .description(
                                                        "운임 요금(청소년(13~18) : 운임에서 350원을 공제한 금액의 20%할인,"
                                                                + " 어린이(6~12) : 운임에서 350원을 공제한 금액의 50%할인)")
                                )
                        )
                )
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1L)
                .queryParam("target", 2L)
                .queryParam("type", "DISTANCE")
                .when().get("/paths")
                .then().log().all().extract();
    }
}

