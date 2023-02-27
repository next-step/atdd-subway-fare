package nextstep.subway.documentation;

import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static nextstep.subway.acceptance.PathSteps.baseDocumentRequest;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;


    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역"),
                new StationResponse(2L, "역삼역")
            ), 10
        );

        when(pathService.findPath(anyLong(), anyLong())).thenReturn(pathResponse);

        baseDocumentRequest(spec)
            .queryParam("source", 1L)
            .queryParam("target", 2L)
            .when().get("/paths")
            .then().log().all().extract();
    }
}
