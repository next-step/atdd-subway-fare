package nextstep.documentation;

import nextstep.domain.Station;
import nextstep.dto.PathResponse;
import nextstep.service.PathService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static nextstep.acceptance.commonStep.PathStep.지하철_경로_조회;
import static org.mockito.Mockito.when;


public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {

        final var response = PathResponse.createPathResponse(List.of(
                new Station(1L, "강남역"),
                new Station(2L, "역삼역")
        ), 10);

        when(pathService.getPath(1L, 2L)).thenReturn(response);

        지하철_경로_조회(1L,2L,createRequestSpecification("path"));

    }
}
