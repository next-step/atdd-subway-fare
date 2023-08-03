package nextstep.subway.documentation;

import io.restassured.response.ValidatableResponse;
import nextstep.subway.domain.enums.PathType;
import nextstep.subway.controller.resonse.PathResponse;
import nextstep.subway.controller.resonse.StationResponse;
import nextstep.subway.service.PathFindService;
import nextstep.subway.steps.PathSteps;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import java.util.List;

import static nextstep.utils.AcceptanceTestUtils.verifyResponseStatus;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;


class PathDocumentation extends Document {

    @MockBean
    private PathFindService pathFindService;

    private Long 강남역_id = 1L;
    private Long 역삼역_id = 2L;

    private String 강남역_이름 = "강남역";
    private String 역삼역_이름 = "역삼역";

    @Test
    void path() {
        ValidatableResponse pathFoundResponse = PathSteps.getPath(강남역_id, 역삼역_id, PathType.DISTANCE);

        verifyResponseStatus(pathFoundResponse, HttpStatus.OK);

        PathSteps.verifyFoundPath(pathFoundResponse, 10L, 40, 0, 강남역_이름, 역삼역_이름);
    }

    @Override
    protected RestDocumentationFilter getDocumentationFilter() {
        final StationResponse 강남역 = new StationResponse(강남역_id, 강남역_이름);
        final StationResponse 역삼역 = new StationResponse(역삼역_id, 역삼역_이름);

        final PathResponse pathResponse = new PathResponse(List.of(강남역, 역삼역), 10L, 40, 0);

        given(pathFindService.getPath(강남역_id, 역삼역_id, PathType.DISTANCE)).willReturn(pathResponse);

        return document("path",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                        parameterWithName("source").description("출발 역 id"),
                        parameterWithName("target").description("도착 역 id"),
                        parameterWithName("type").description("경로 조회 타입")
                ),
                responseFields(
                        fieldWithPath(".distance").description("경로 거리"),
                        fieldWithPath(".duration").description("경로 시간"),
                        fieldWithPath(".fare").description("요금"),
                        fieldWithPath(".stationResponses[]").description("경로 내 지하철 역 목록"),
                        fieldWithPath(".stationResponses[].id").description("지하철 역 id"),
                        fieldWithPath(".stationResponses[].name").description("지하철 역 이름")
                )
        );
    }
}
