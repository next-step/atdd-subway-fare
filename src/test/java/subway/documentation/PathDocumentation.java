package subway.documentation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import subway.acceptance.path.PathSteps;
import subway.path.application.PathService;
import subway.path.application.dto.PathRetrieveResponse;
import subway.station.application.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class PathDocumentation extends Documentation {
    @MockBean
    private PathService pathService;

    @Test
    void path() {
        // given
        StationResponse 강남역 = StationResponse.builder().id(1L).name("강남역").build();
        StationResponse 역삼역 = StationResponse.builder().id(2L).name("역삼역").build();
        PathRetrieveResponse pathRetrieve = PathRetrieveResponse.builder()
                .stations(List.of(강남역, 역삼역))
                .distance(10)
                .build();
        when(pathService.getShortestPath(anyLong(), anyLong())).thenReturn(pathRetrieve);

        // when
        var response = PathSteps.getShortestPathForDocument(강남역.getId(),
                역삼역.getId(),
                this.spec,
                PathSteps.경로_필터());

        // then
        var list = response.jsonPath().getList("stations.name", String.class);
        assertThat(list).containsExactlyInAnyOrder("강남역", "역삼역");
    }


}
