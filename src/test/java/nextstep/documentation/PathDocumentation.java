package nextstep.documentation;

import nextstep.domain.subway.Station;
import nextstep.domain.subway.PathType;
import nextstep.dto.PathResponse;
import nextstep.service.PathService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static nextstep.acceptance.commonStep.PathStep.지하철_경로_조회;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;


    @BeforeEach
    public void setGivenData() {



    }
    @Test
    void path() {

        Station 교대역 = new Station(1L, "교대역");
        Station 강남역 = new Station(2L, "강남역");

        Long 교대강남구간거리 = 10L;
        Long 교대강남구간시간 = 5L;
        int 교대강남구간비용 = 1250;


        final PathResponse pathResponse = new PathResponse(List.of(교대역, 강남역), 교대강남구간거리, 교대강남구간시간, 교대강남구간비용);
        when(pathService.getPath(any(), any(), any(),any())).thenReturn(pathResponse);

        지하철_경로_조회(1L,2L, PathType.DISTANCE,createRequestSpecification("path"));

    }
}
