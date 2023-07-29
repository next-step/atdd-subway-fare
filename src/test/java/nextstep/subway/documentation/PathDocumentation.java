package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.controller.resonse.PathResponse;
import nextstep.subway.controller.resonse.StationResponse;
import nextstep.subway.service.PathFindService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;


class PathDocumentation extends Document {

    @MockBean
    private PathFindService pathFindService;

    private Long 강남역Id = 1L;
    private Long 역삼역Id = 2L;

    @Test
    void path() {
        RestAssured
                .given(spec).log().all()
                .filter(getDocumentationFilter())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 강남역Id)
                .queryParam("target", 역삼역Id)
                .when().get("/paths")
                .then().log().all().extract();
    }

    private RestDocumentationFilter getDocumentationFilter() {
        final StationResponse 강남역 = new StationResponse(강남역Id, "강남역");
        final StationResponse 역삼역 = new StationResponse(역삼역Id, "역삼역");

        final PathResponse pathResponse = new PathResponse(List.of(강남역, 역삼역), 10L);

        given(pathFindService.getShortestPath(강남역Id, 역삼역Id)).willReturn(pathResponse);

        return document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath(".distance").description("경로 거리"),
                        fieldWithPath(".stationResponses[]").description("경로 내 지하철 역 목록"),
                        fieldWithPath(".stationResponses[].id").description("지하철 역 id"),
                        fieldWithPath(".stationResponses[].name").description("지하철 역 이름")
                )
        );
    }
}
