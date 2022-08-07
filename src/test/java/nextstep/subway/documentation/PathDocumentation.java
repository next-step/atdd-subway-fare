package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static nextstep.subway.acceptance.PathSteps.경로_조회;
import static nextstep.subway.domain.PathType.DISTANCE;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        // given
        var 강남역 = new StationResponse(1L, "강남역");
        var 역삼역 = new StationResponse(2L, "역삼역");
        var pathResponse = new PathResponse(List.of(강남역, 역삼역), 10, 5, 1250);

        // when
        when(pathService.findPath(any(), anyInt())).thenReturn(pathResponse);

        // then
        경로_조회(spec, 강남역.getId(), 역삼역.getId(), DISTANCE);
    }
}
