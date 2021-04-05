package nextstep.subway.path.documentation;

import nextstep.subway.Documentation;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static nextstep.subway.path.acceptance.PathSteps.*;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        //given
        PathResponse pathResponse = 응답_만들기(30, 10, 1650,
                역_만들기(1L, "강남역"),
                역_만들기(2L, "역삼역"),
                역_만들기(3L, "선릉역")
        );

        //when
        경로_탐색시(pathService, pathResponse);

        //then
        문서화_두_역의_최단_거리_경로_조회를_요청(spec,1L, 3L);
    }
}

