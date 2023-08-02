package nextstep.subway.documentation;

import static nextstep.subway.acceptance.PathSteps.경로_조회_예시_응답;
import static nextstep.subway.acceptance.PathSteps.두_역의_최단_거리_경로_조회를_요청;
import static nextstep.subway.acceptance.PathSteps.출력_필드_추가;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import nextstep.subway.applicaion.PathService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class PathDocumentation extends Documentation {

    @MockBean
    PathService pathService;

    @DisplayName("경로 조회 문서")
    @Test
    void findPath() {
        var pathResponse = 경로_조회_예시_응답();

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);
        출력_필드_추가("findPath", spec);

        두_역의_최단_거리_경로_조회를_요청(1L, 2L, spec);
    }
}
