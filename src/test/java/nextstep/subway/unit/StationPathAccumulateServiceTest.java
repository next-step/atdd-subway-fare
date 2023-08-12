package nextstep.subway.unit;

import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.repository.StationLineRepository;
import nextstep.subway.domain.StationLineSection;
import nextstep.subway.domain.service.path.StationPathAccumulateService;
import nextstep.subway.domain.service.aggregation.StationPathAggregationService;
import nextstep.subway.unit.fixture.StationLineSpec;
import nextstep.subway.unit.fixture.StationSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.utils.UnitTestUtils.createEntityTestIds;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class StationPathAccumulateServiceTest {
    @InjectMocks
    StationPathAccumulateService stationPathAccumulateService;

    @Mock
    StationLineRepository stationLineRepository;

    @Spy
    StationPathAggregationService stationPathAggregationService;
    Map<String, Station> stationByName;
    Map<String, Long> stationIdByName;
    Map<Long, StationLineSection> sectionByDownStationId;
    List<Station> stations;
    List<StationLine> stationLines;
    List<StationLineSection> stationLineSections;

    static final Long ONE_MIN = 1000 * 60L;

    @BeforeEach
    void setUp() {
        //given
        stations = StationSpec.of(List.of("A역", "B역", "C역", "D역", "E역", "F역", "G역", "H역", "I역", "Y역", "Z역"));
        stationByName = stations.stream()
                .collect(Collectors.toMap(Station::getName, Function.identity()));
        stationIdByName = stations.stream()
                .collect(Collectors.toMap(Station::getName, Station::getId));

        //A,B,C
        final StationLine line_1 = StationLineSpec.of(stationByName.get("A역"), stationByName.get("B역"), BigDecimal.valueOf(8L), ONE_MIN * 4, BigDecimal.ZERO);
        line_1.createSection(stationByName.get("B역"), stationByName.get("C역"), BigDecimal.ONE, ONE_MIN * 2);

        //C,D,E
        final StationLine line_2 = StationLineSpec.of(stationByName.get("C역"), stationByName.get("D역"), BigDecimal.valueOf(9L), ONE_MIN * 5, BigDecimal.ZERO);
        line_2.createSection(stationByName.get("D역"), stationByName.get("E역"), BigDecimal.valueOf(7L), ONE_MIN * 3);

        //E,F,G
        final StationLine line_3 = StationLineSpec.of(stationByName.get("E역"), stationByName.get("F역"), BigDecimal.valueOf(4L), ONE_MIN * 4, BigDecimal.ZERO);
        line_3.createSection(stationByName.get("F역"), stationByName.get("G역"), BigDecimal.valueOf(3L), ONE_MIN * 4);

        //G,H,I,A
        final StationLine line_4 = StationLineSpec.of(stationByName.get("G역"), stationByName.get("H역"), BigDecimal.ONE, ONE_MIN, BigDecimal.ZERO);
        line_4.createSection(stationByName.get("H역"), stationByName.get("I역"), BigDecimal.valueOf(7L), ONE_MIN * 5);
        line_4.createSection(stationByName.get("I역"), stationByName.get("A역"), BigDecimal.valueOf(2L), ONE_MIN * 6);

        //Y,Z
        final StationLine line_5 = StationLineSpec.of(stationByName.get("Y역"), stationByName.get("Z역"), BigDecimal.valueOf(4L), ONE_MIN * 3, BigDecimal.ZERO);

        stationLines = List.of(line_1, line_2, line_3, line_4, line_5);
        createEntityTestIds(stationLines, 1L);

        stationLineSections = stationLines.stream()
                .map(StationLine::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        sectionByDownStationId = stationLineSections.stream()
                .collect(Collectors.toMap(StationLineSection::getDownStationId, Function.identity()));
        createEntityTestIds(stationLineSections, 1L);

        final List<StationLineSection> stationLineSections = stationLines.stream()
                .map(StationLine::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        createEntityTestIds(stationLineSections, 1L);
    }

    @DisplayName("역 id 목록의 전체 거리 계산")
    @Test
    void accumulateTotalDistance() {
        //given
        given(stationLineRepository.findAll()).willReturn(stationLines);

        final List<Long> pathStationIds = Stream.of("A역", "I역", "H역", "G역", "F역", "E역")
                .map(stationIdByName::get)
                .collect(Collectors.toList());

        //when
        final BigDecimal distance = stationPathAccumulateService.accumulateTotalDistance(pathStationIds);

        //then
        //A - 2km - I - 7km - H - 1km - G - 3km - F - 4km - E
        final BigDecimal expectedDistance = BigDecimal.valueOf(17);
        Assertions.assertEquals(0, distance.compareTo(expectedDistance));
    }

    @DisplayName("역 id 목록의 전체 소요시간 계산")
    @Test
    void accumulateTotalDuration() {
        //given
        given(stationLineRepository.findAll()).willReturn(stationLines);

        final List<Long> pathStationIds = Stream.of("A역", "I역", "H역", "G역", "F역", "E역")
                .map(stationIdByName::get)
                .collect(Collectors.toList());

        //when
        final Long duration = stationPathAccumulateService.accumulateTotalDuration(pathStationIds);

        //then
        //A - 6min - I - 5min - H - 1min - G - 4min - F - 4min - E
        final Long expectedDuration = 1000 * 60 * 20L;
        Assertions.assertEquals(expectedDuration, duration);
    }
}
