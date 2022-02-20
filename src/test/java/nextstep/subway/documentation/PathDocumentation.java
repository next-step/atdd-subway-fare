package nextstep.subway.documentation;

import static nextstep.subway.acceptance.PathSteps.두_역의_경로_조회를_요청;
import static nextstep.subway.documentation.PathDocumentationSteps.getFilteredRequestSpecification;
import static nextstep.subway.documentation.PathDocumentationSteps.getSearchPathDocumentFilter;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import io.restassured.specification.RequestSpecification;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.PathSearchType;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        PathResponse pathResponse = new PathResponse(
            Lists.newArrayList(
                new StationResponse(1L, "강남역", null, null),
                new StationResponse(2L, "역삼역", null, null)
            ), 10, 10
        );

        when(pathService.findPath(anyLong(), anyLong(), any())).thenReturn(pathResponse);

        RequestSpecification filteredRequestSpecification = getFilteredRequestSpecification(spec, getSearchPathDocumentFilter("path"));

        두_역의_경로_조회를_요청(filteredRequestSpecification, 1L, 2L, PathSearchType.DISTANCE);
    }
}
