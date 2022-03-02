package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static nextstep.subway.documentation.PathSteps.createPathResponse;
import static nextstep.subway.documentation.PathSteps.경로_조회_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    PathService pathService;

    @Test
    void path() {
        when(pathService.findPath(any())).thenReturn(createPathResponse());
        경로_조회_요청(spec);
    }

}
