package nextstep.subway.documentation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static nextstep.subway.documentation.DocumentationHelper.경로_조회_성공;
import static nextstep.subway.documentation.DocumentationHelper.경로_조회_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;
    private PathResponse pathResponse;

    @BeforeEach
    void setFixtures() {
        pathResponse = new PathResponse(
                Lists.newArrayList(
                        new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "역삼역", LocalDateTime.now(), LocalDateTime.now())
                ), 10, 10
        );
        ReflectionTestUtils.setField(pathResponse, "fare", 1250);
    }

    @EnumSource(PathType.class)
    @ParameterizedTest
    void 경로_조회(PathType pathType) {
        when(pathService.findPath(any(), anyInt())).thenReturn(pathResponse);
        ExtractableResponse<Response> response = 경로_조회_요청(spec, pathType);
        경로_조회_성공(response);
    }
}
