package nextstep.subway.unit;

import nextstep.subway.domain.*;
import nextstep.subway.domain.repository.StationLineRepository;
import nextstep.subway.domain.repository.StationRepository;
import nextstep.subway.service.StationLineService;
import nextstep.subway.service.dto.StationLineSectionCreateRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@SpringBootTest
@Transactional
public class StationLineServiceTest {

    @Autowired
    private StationLineService stationLineService;

    @Autowired
    private StationLineRepository stationLineRepository;

    @Autowired
    private StationRepository stationRepository;


    @DisplayName("정상적인 노선의 역 구간 등록")
    @Test
    void createStationLineSection() {
        //given
        final Station aStation = new Station("A역");
        final Station bStation = new Station("B역");
        final Station cStation = new Station("B역");

        stationRepository.save(aStation);
        final Station savedBStation = stationRepository.save(bStation);
        final Station savedCStation = stationRepository.save(cStation);

        final StationLine line = StationLine.builder()
                .name("1호선")
                .color("blue")
                .upStation(aStation)
                .downStation(bStation)
                .distance(BigDecimal.TEN)
                .build();

        final StationLine savedStationLine = stationLineRepository.save(line);

        //when
        var request = new StationLineSectionCreateRequest(savedBStation.getId(), savedCStation.getId(), BigDecimal.ONE, 1000L);
        Assertions.assertDoesNotThrow(() -> stationLineService.createStationLineSection(savedStationLine.getLineId(), request));

        //then
        final StationLine result = stationLineRepository.findById(savedStationLine.getLineId()).get();

        Assertions.assertTrue(result.getAllStations().contains(savedCStation));
    }

    @DisplayName("정상적인 노선의 역 구간 삭제")
    @Test
    void deleteStationLineSection() {
        //given
        final Station aStation = new Station("A역");
        final Station bStation = new Station("B역");
        final Station cStation = new Station("B역");

        stationRepository.save(aStation);
        final Station savedBStation = stationRepository.save(bStation);
        final Station savedCStation = stationRepository.save(cStation);

        final StationLine line = StationLine.builder()
                .name("1호선")
                .color("blue")
                .upStation(aStation)
                .downStation(bStation)
                .distance(BigDecimal.TEN)
                .build();

        final StationLine savedStationLine = stationLineRepository.save(line);

        var request = new StationLineSectionCreateRequest(savedBStation.getId(), savedCStation.getId(), BigDecimal.ONE, 1000L);
        stationLineService.createStationLineSection(savedStationLine.getLineId(), request);

        //when
        Assertions.assertDoesNotThrow(() -> stationLineService.deleteStationLineSection(savedStationLine.getLineId(), savedCStation.getId()));

        //then
        final StationLine result = stationLineRepository.findById(savedStationLine.getLineId()).get();

        Assertions.assertFalse(result.getAllStations().contains(savedCStation));
    }
}
