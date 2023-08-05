package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회를_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        // given
        Long 강남역 = 1L;
        Long 역삼역 = 2L;
        int distance = 10;
        int duration = 3;
        int fare = 1250;

        PathResponse pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(강남역, "강남역"),
                        new StationResponse(역삼역, "역삼역")
                )
                , distance
                , duration
                , fare
        );

        when(pathService.findPath(anyLong(), anyLong(), any(), anyString())).thenReturn(pathResponse);

        RestDocumentationFilter restDocumentationFilter = document("path"
                , preprocessRequest(prettyPrint())
                , preprocessResponse(prettyPrint())
                , requestParameters(
                        parameterWithName("source").description("출발역 id")
                        , parameterWithName("target").description("도착역 id")
                        , parameterWithName("type").description("조회타입, DISTANCE(거리) or DURATION(시간)")
                )
                , responseFields(
                        subsectionWithPath("stations").description("출발역으로부터 도착역까지의 경로에 있는 역 목록")
                        , subsectionWithPath("distance").description("조회한 경로 구간의 거리")
                        , subsectionWithPath("duration").description("조회한 경로 구간의 소요시간")
                        , subsectionWithPath("fare").description("지하철 이용 요금")
                )
        );
        spec.filter(restDocumentationFilter);

        // when
        var response = 두_역의_경로_조회를_요청(spec, 강남역, 역삼역, PathType.DURATION.toString());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(강남역, 역삼역);
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(distance);
        assertThat(response.jsonPath().getInt("duration")).isEqualTo(duration);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(fare);
    }
}
