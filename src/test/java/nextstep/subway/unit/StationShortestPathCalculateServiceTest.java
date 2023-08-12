package nextstep.subway.unit;

import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.repository.StationLineRepository;
import nextstep.subway.domain.StationLineSection;
import nextstep.subway.domain.service.path.StationPathSearchRequestType;
import nextstep.subway.domain.service.path.StationShortestPathCalculateService;
import nextstep.subway.unit.fixture.StationLineSpec;
import nextstep.subway.unit.fixture.StationSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static nextstep.utils.UnitTestUtils.createEntityTestIds;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class StationShortestPathCalculateServiceTest {
    @InjectMocks
    StationShortestPathCalculateService stationShortestPathCalculateService;

    @Mock
    StationLineRepository stationLineRepository;

    Map<String, Station> stationByName;
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
        createEntityTestIds(stationLineSections, 1L);

        final List<StationLineSection> stationLineSections = stationLines.stream()
                .map(StationLine::getSections)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        createEntityTestIds(stationLineSections, 1L);
    }

    @DisplayName("지하철 최단거리 경로의 역id 목록 조회")
    @Test
    void searchStationPathWithShortestDistance() {
        //given
        given(stationLineRepository.findAll()).willReturn(stationLines);

        //when
        final List<Long> pathStationIds = stationShortestPathCalculateService.getShortestPathStations(
                stationByName.get("A역"),
                stationByName.get("E역"),
                StationPathSearchRequestType.DISTANCE);

        //then
        final Map<Long, Station> stationById = stations.stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));

        final List<String> expectedPathStationNames = List.of("A역", "I역", "H역", "G역", "F역", "E역");

        Assertions.assertArrayEquals(expectedPathStationNames.toArray(), pathStationIds
                .stream()
                .map(stationById::get)
                .map(Station::getName).toArray());
    }

    @DisplayName("지하철 최소시간 경로의 역id 목록 조회")
    @Test
    void searchStationPathWithShortestDuration() {
        //given
        given(stationLineRepository.findAll()).willReturn(stationLines);

        //when
        final List<Long> pathStationIds = stationShortestPathCalculateService.getShortestPathStations(
                stationByName.get("A역"),
                stationByName.get("E역"),
                StationPathSearchRequestType.DURATION);

        //then
        final Map<Long, Station> stationById = stations.stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));

        final List<String> expectedPathStationNames = List.of("A역", "B역", "C역", "D역", "E역");

        Assertions.assertArrayEquals(expectedPathStationNames.toArray(), pathStationIds
                .stream()
                .map(stationById::get)
                .map(Station::getName).toArray());
    }


    @DisplayName("출발역과 도착역 간의 경로가 존재하지 않는 경우 예외")
    @Test
    void searchStationPath_Not_Exists_Path_Between_SourceStation_TargetStation() {
        //when
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> stationShortestPathCalculateService.getShortestPathStations(
                        stationByName.get("A역"),
                        stationByName.get("Z역"),
                        StationPathSearchRequestType.DISTANCE));
    }
}