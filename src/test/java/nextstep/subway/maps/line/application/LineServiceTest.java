package nextstep.subway.maps.line.application;

import nextstep.subway.maps.line.domain.Line;
import nextstep.subway.maps.line.domain.LineRepository;
import nextstep.subway.maps.line.domain.LineStation;
import nextstep.subway.maps.line.dto.LineResponse;
import nextstep.subway.utils.TestObjectUtils;
import nextstep.subway.maps.station.application.StationService;
import nextstep.subway.maps.station.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LineServiceTest {
    private Map<Long, Station> stations;
    private Line line;

    @BeforeEach
    void setUp() {
        Station station1 = TestObjectUtils.createStation(1L, "강남역");
        Station station2 = TestObjectUtils.createStation(2L, "역삼역");
        stations = Lists.newArrayList(station1, station2).stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));

        line = TestObjectUtils.createLine(1L, "신분당선", "RED");
        line.addLineStation(new LineStation(1L, null, 10, 10));
        line.addLineStation(new LineStation(2L, 1L, 10, 10));
    }

    @Test
    void findLineResponsesById() {
        LineRepository lineRepository = mock(LineRepository.class);
        StationService stationService = mock(StationService.class);
        LineService lineService = new LineService(lineRepository, stationService);

        when(lineRepository.findById(anyLong())).thenReturn(Optional.of(line));
        when(stationService.findStationsByIds(anyList())).thenReturn(stations);

        LineResponse result = lineService.findLineResponsesById(line.getId());

        assertThat(result.getStations()).hasSize(2);
    }
}
