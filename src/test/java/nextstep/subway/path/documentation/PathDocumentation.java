package nextstep.subway.path.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.common.Documentation;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.common.DocumentationUtils.given;
import static nextstep.subway.path.acceptance.PathUtils.두_역의_경로_조회를_요청;
import static nextstep.subway.path.documentation.PathDocumentationUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {
    @MockBean
    PathService pathService;

    /*
        <질문>
        문서화 테스트에서 검증과정인 then 부분이 필요한지 궁금합니다~
     */
    @ParameterizedTest(name = "경로 조회 문서화 [{index}] [{arguments}]")
    @EnumSource(PathType.class)
    void path(PathType type) {
        // given
        PathResponse pathResponse = new PathResponse(
                Arrays.asList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ),
                10, 10
        );

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        RequestSpecification requestSpecification =
                given(spec, "path", getRequestParameterSnippet(), getResponseFieldsSnippet());

        Map<String, Object> params = new HashMap<>();
        params.put("source", 1L);
        params.put("target", 2L);
        params.put("type", type.name());

        // when
        ExtractableResponse<Response> 조회_응답 = 두_역의_경로_조회를_요청(requestSpecification, params);

        // then
        경로_조회_성공(조회_응답);
    }
}
