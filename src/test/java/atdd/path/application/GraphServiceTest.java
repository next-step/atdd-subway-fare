package atdd.path.application;

import atdd.path.application.dto.MinTimePathResponseView;
import atdd.path.dao.LineDao;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static atdd.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = GraphService.class)
public class GraphServiceTest {
    private GraphService graphService;

    @MockBean
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        this.graphService = new GraphService(lineDao);
    }

    @DisplayName("출발역과 도착역 사이의 최단경로 Station 목록을 응답받기")
    @Test
    public void findPath() {
        Long startId = 1L;
        Long endId = 3L;
        List<Line> lines = Lists.list(TEST_LINE, TEST_LINE_2, TEST_LINE_3, TEST_LINE_4);
        given(lineDao.findAll()).willReturn(lines);

        List<Station> shortestPath = graphService.findPath(startId, endId);

        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat(shortestPath.get(0)).isEqualTo(TEST_STATION);
        assertThat(shortestPath.get(2)).isEqualTo(TEST_STATION_3);
    }

    @DisplayName("출발역과 도착역 사이의 최소 시간 경로 구하기")
    @Test
    public void findMinTimePath() {
        //given
        Long startId = 1L;
        Long endId = 3L;
        List<Line> lines = Lists.list(TEST_LINE, TEST_LINE_2, TEST_LINE_3, TEST_LINE_4);
        given(lineDao.findAll()).willReturn(lines);

        //when
        MinTimePathResponseView minTimePath = graphService.findMinTimePath(startId, endId);

        //then
        assertThat(minTimePath.getStartStationId()).isEqualTo(1L);
        assertThat(minTimePath.getEndStationId()).isEqualTo(3L);
        assertThat(minTimePath.getLines().size()).isEqualTo(1);
        assertThat(minTimePath.getStations().size()).isEqualTo(3);
        assertThat(minTimePath.getDistance()).isEqualTo(20.0);
        assertThat(minTimePath.getDepartAt()).isNotNull();
        assertThat(minTimePath.getArriveBy()).isNotNull();
    }
}

