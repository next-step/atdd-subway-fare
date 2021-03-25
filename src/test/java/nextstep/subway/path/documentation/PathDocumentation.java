package nextstep.subway.path.documentation;

import io.restassured.RestAssured;
import nextstep.subway.Documentation;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    @DisplayName("두_역의_최단_거리_경로_조회를_요청")
    void path() {
        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
            ),
            10,
            10
        );

        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class)))
            .thenReturn(pathResponse);

        RestAssured.given(spec).log().all()
            .filter(
                document(
                    "path",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("stations[].id").type(JsonFieldType.NUMBER).description("역 아이디"),
                        fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("역 이름"),
                        fieldWithPath("stations[].createdDate").type(JsonFieldType.STRING).description("생성일"),
                        fieldWithPath("stations[].modifiedDate").type(JsonFieldType.STRING).description("수정일"),
                        fieldWithPath("distance").type(JsonFieldType.NUMBER).description("경로에 대한 거리"),
                        fieldWithPath("duration").type(JsonFieldType.NUMBER).description("경로에 대한 시간")
                    )
                )
            )
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("source", 1L)
            .queryParam("target", 2L)
            .queryParam("type", "DISTANCE")
            .when().get("/paths")
            .then().log().all();
    }
}

