package nextstep.subway.path.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.common.Documentation;
import nextstep.subway.path.acceptance.PathUtils;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import static nextstep.subway.common.DocumentationUtils.given;
import static nextstep.subway.path.acceptance.PathUtils.두_역의_경로_조회를_요청;
import static nextstep.subway.path.documentation.PathDocumentationUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {
    @MockBean
    PathService pathService;

    @ParameterizedTest(name = "경로 조회 문서화 [{index}] [{arguments}]")
    @EnumSource(PathType.class)
    void path(PathType type) {
        // given
        when(pathService.findPath(anyInt(), anyLong(), anyLong(), any())).thenReturn(getPathResponse());

        RequestSpecification requestSpecification =
                given(spec, "path", getRequestParameterSnippet(), getResponseFieldsSnippet());

        Map<String, Object> params = PathUtils.경로_조회_요청_파라미터(1L, 2L, type);

        // when
        ExtractableResponse<Response> response = 두_역의_경로_조회를_요청(requestSpecification, params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
