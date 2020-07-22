package nextstep.subway.maps.station.application;

import com.google.common.collect.Lists;
import nextstep.subway.utils.TestObjectUtils;
import nextstep.subway.maps.station.domain.Station;
import nextstep.subway.maps.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StationServiceTest {

    @DisplayName("지하철역을 Map형태로 조회")
    @Test
    void findStationsByIds() {
        StationRepository stationRepository = mock(StationRepository.class);
        StationService stationService = new StationService(stationRepository);
        ArrayList<Station> persistStations = Lists.newArrayList(
                TestObjectUtils.createStation(1L, "강남역"),
                TestObjectUtils.createStation(2L, "역삼역"),
                TestObjectUtils.createStation(3L, "선릉역")
        );

        when(stationRepository.findAllById(anyList())).thenReturn(persistStations);

        Map<Long, Station> stations = stationService.findStationsByIds(Lists.newArrayList(1L, 2L, 3L));

        assertThat(stations).hasSize(3);
        assertThat(stations.get(1L).getName()).isEqualTo("강남역");
    }
}