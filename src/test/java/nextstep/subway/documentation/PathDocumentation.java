package nextstep.subway.documentation;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.WeightType;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역", null, null),
                new StationResponse(2L, "역삼역", null, null)
            ), 10, 5
        );

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        RestAssured
            .given(spec).log().all()
            .filter(document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("source").description("출발역 아이디"),
                    parameterWithName("target").description("도착역 아이디"),
                    parameterWithName("weightType").description("조회 조건")
                ),
                responseFields(
                    fieldWithPath("stations").description("경로 지하철역 목록"),
                    fieldWithPath("stations[].id").description("지하철역 아이디"),
                    fieldWithPath("stations[].name").description("지하철역 이름"),
                    fieldWithPath("stations[].createdDate").description("역 생성 일자"),
                    fieldWithPath("stations[].modifiedDate").description("역 수정 일자"),
                    fieldWithPath("distance").description("거리(km)"),
                    fieldWithPath("duration").description("소요 시간(min)")
                )
            ))
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 1L)
            .queryParam("target", 2L)
            .queryParams("weightType", WeightType.DURATION)
            .when().get("/paths")
            .then().log().all().extract();
    }
}
