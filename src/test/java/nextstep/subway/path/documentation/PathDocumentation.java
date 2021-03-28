package nextstep.subway.path.documentation;

import static nextstep.subway.path.acceptance.PathSteps.*;
import static nextstep.subway.utils.DocumentationUtil.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import java.time.LocalDateTime;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;

import io.restassured.specification.RequestSpecification;
import nextstep.subway.Documentation;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                new StationResponse(1L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
            ), 10, 10
        );
        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        final RequestParametersSnippet requestParametersSnippet = requestParameters(
            parameterWithName("source").description("출발역 식별자"),
            parameterWithName("target").description("도착역 식별자"),
            parameterWithName("type").description("경로 찾기 타입"));

        final ResponseFieldsSnippet responseFieldsSnippet = responseFields(
            fieldWithPath("stations[]").type(JsonFieldType.ARRAY).description("최단 경로 역 정보"),
            fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("최단 경로 역 식별자"),
            fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("최단 경로 역 이름"),
            fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("최단 경로 역 생성시간"),
            fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("최단 경로 역 수정시간"),
            fieldWithPath("distance").type(JsonFieldType.NUMBER).description("최단 경로 거리"),
            fieldWithPath("duration").type(JsonFieldType.NUMBER).description("최단 경로 시간")
        );

        RequestSpecification requestSpecification =
            given(spec, "path", requestParametersSnippet, responseFieldsSnippet);

        두_역의_최단_거리_경로_조회를_요청(requestSpecification, 1L, 2L);
    }
}

