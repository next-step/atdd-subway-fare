package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PathServiceTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @Autowired
    private PathService pathService;

    private Station 교대역;
    private Station 강남역;
    private Station 역삼역;
    private Station 양재역;

    private Line 이호선;
    private Line 삼호선;

    /**
     *
     * 교대--(5,5)---강남
     *  |(5,5)       |(20,5)
     * 양재--(5,20)--역삼
     */
    @BeforeEach
    void setUp() {
        교대역 = stationRepository.save(new Station("교대역"));
        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));
        양재역 = stationRepository.save(new Station("양재역"));

        이호선 = lineRepository.save(createLine("이호선", 교대역, 강남역, 500));
        lineService.addSection(이호선.getId(), createSectionRequest(강남역, 20, 5));

        삼호선 = lineRepository.save(createLine("삼호선", 교대역, 양재역, 1000));
        lineService.addSection(삼호선.getId(), createSectionRequest(양재역, 5, 20));
    }

    @ParameterizedTest(name = "거리비례제 요금 조회 [{arguments}]")
    @MethodSource
    void findPath(PathType pathType, int expected) {
        //when
        PathResponse path = pathService.findPath(교대역.getId(), 역삼역.getId(), pathType);
        int fare = path.getFare();

        //then
        assertThat(fare).isEqualTo(expected);
    }

    private static Stream<Arguments> findPath() {
        return Stream.of(
                Arguments.of(PathType.DISTANCE, 1250),
                Arguments.of(PathType.DURATION, 1650)
        );
    }

    @DisplayName("여러 노선 환승 시 가장 높은 추가요금만 적용")
    @Test
    void applyHighestExtraCharge() {
        //when
        PathResponse path = pathService.findPath(교대역.getId(), 역삼역.getId(), PathType.DISTANCE);
        int fare = path.getExtraCharge();

        //then
        assertThat(fare).isEqualTo(1000);
    }

    private Line createLine(String name, Station upStation, Station downStation, int extraCharge) {
        Line line = new Line(name, "green", extraCharge);
        line.addSection(upStation, downStation, 5, 5);
        return line;
    }

    private SectionRequest createSectionRequest(Station 강남역, int distance, int duration) {
        return new SectionRequest(강남역.getId(), 역삼역.getId(), distance, duration);
    }
}
