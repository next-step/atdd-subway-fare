package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회를_요청;
import static nextstep.subway.documentation.PathDocumentationFixture.*;
import static nextstep.subway.documentation.PathDocumentationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        // given
        when(pathService.findPath(anyLong(), anyLong(), any(PathType.class), anyInt())).thenReturn(PATH_RESPONSE);

        // when
        RestDocumentationFilter documentFilter = getDocumentFilter(getRequestParameters(), getResponseFields());
        ExtractableResponse<Response> response =
                두_역의_경로_조회를_요청(1L, 2L, PathType.DISTANCE, getSpecification(spec, documentFilter));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
