package nextstep.subway.maps.line.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Maps;
import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.line.dto.LineStationCreateRequest;
import nextstep.subway.maps.station.application.StationService;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.utils.TestObjectUtils;

@DisplayName("지하철 노선 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class LineStationServiceTest {
    @Mock
    private LineService lineService;
    @Mock
    private StationService stationService;

    private LineStationService lineStationService;

    @BeforeEach
    void setUp() {
        lineStationService = new LineStationService(lineService, stationService);
    }

    @DisplayName("지하철 노선에 역을 등록한다.")
    @Test
    void addLineStation1() {
        // given
        Station station1 = TestObjectUtils.createStation(1L, "강남역");
        Station station2 = TestObjectUtils.createStation(2L, "양재역");
        Map<Long, Station> stations = new HashMap<>();
        stations.put(1L, station1);
        stations.put(2L, station2);
        when(stationService.findStationsByIds(anyList())).thenReturn(stations);

        Line line = TestObjectUtils.createLine(1L, "신분당선", "RED", 0, 10);
        when(lineService.findLineById(anyLong())).thenReturn(line);

        // when
        LineStationCreateRequest request = new LineStationCreateRequest(1L, null, 10, 10);
        lineStationService.addLineStation(1L, request);

        // then
        assertThat(line.getStationInOrder()).hasSize(1);
    }

    @DisplayName("존재하지 않는 역을 등록한다.")
    @Test
    void addLineStation2() {
        // given
        Station station = new Station();
        ReflectionTestUtils.setField(station, "id", 1L);
        when(stationService.findStationsByIds(anyList())).thenReturn(Maps.newHashMap());

        // when
        LineStationCreateRequest request = new LineStationCreateRequest(1L, null, 10, 10);
        assertThatThrownBy(() -> lineStationService.addLineStation(1L, request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("지하철 노선에 역을 제외한다.")
    @Test
    void removeLineStation() {
        // given
        Line line = TestObjectUtils.createLine(1L, "신분당선", "RED", 0, 10);
        line.addLineStation(new LineStation(1L, null, 10, 10));
        line.addLineStation(new LineStation(2L, 1L, 10, 10));
        when(lineService.findLineById(anyLong())).thenReturn(line);

        // when
        lineStationService.removeLineStation(1L, 2L);

        // then
        assertThat(line.getStationInOrder()).hasSize(1);
    }
}
