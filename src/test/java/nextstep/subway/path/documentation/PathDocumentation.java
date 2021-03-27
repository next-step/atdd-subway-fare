package nextstep.subway.path.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.Documentation;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static nextstep.subway.path.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static org.mockito.ArgumentMatchers.any;
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

class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        // given
        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 10
        );
        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        // when
        RequestSpecification requestSpecification = RestAssured
                .given(spec).log().all()
                .filter(document("path", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("source").description("출발역"),
                                parameterWithName("target").description("도착역"),
                                parameterWithName("type").description("최단 경로 타입")
                        ),
                        responseFields(
                                fieldWithPath("stations[]").description("최단 경로 역 리스트"),
                                fieldWithPath("stations[].id").description("(최단 경로 역) ID"),
                                fieldWithPath("stations[].name").description("(최단 경로 역) 이름"),
                                fieldWithPath("stations[].createdDate").description("(최단 경로 역) 생성일시"),
                                fieldWithPath("stations[].modifiedDate").description("(최단 경로 역) 수정일시"),
                                fieldWithPath("distance").description("최단 경로 거리"),
                                fieldWithPath("duration").description("최단 경로 시간")
                        )
                ));
        두_역의_최단_거리_경로_조회를_요청(requestSpecification, 1L, 2L);
    }
}

