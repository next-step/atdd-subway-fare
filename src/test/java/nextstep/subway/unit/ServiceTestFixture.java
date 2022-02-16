package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ServiceTestFixture {
    @Autowired
    protected StationRepository stationRepository;
    @Autowired
    protected LineRepository lineRepository;

    protected Station 강남역;
    protected Station 양재역;
    protected Station 판교역;
    protected Line 신분당선;

    protected Station 교대역;
    protected Station 남부터미널역;
    protected Line 삼호선;

    protected Line 이호선;

    /**
     * 교대역    --- *2호선(10, 6)* ---    강남역
     * |                                   |
     * *3호선(5, 2)                   *신분당선(5, 4)*
     * |                                   |
     * 남부터미널역  --- *3호선(3, 10)* ---  양재
     *
     * 최단 경로 -> 교대, 남부터미널, 양재 (8, 12)
     * 최소시간 견로 -> 교대, 강남역, 양재 (15, 10)
     */

    @BeforeEach
    public void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("역삼역"));
        판교역 = stationRepository.save(new Station("삼성역"));
        신분당선 = lineRepository.save(createLine("신분당선", "red", 강남역, 양재역, 5, 4));

        교대역 = stationRepository.save(new Station("교대역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));
        삼호선 = lineRepository.save(createLine("3호선", "orange", 교대역, 남부터미널역, 5, 2));

        이호선 = lineRepository.save(createLine("2호선", "green", 교대역, 강남역, 10, 6));

        삼호선.addSection(남부터미널역, 양재역, 3, 10);
    }

    private Line createLine(String name, String color, Station upStation, Station downStation, int distance, int duration) {
        Line line = new Line(name, color);
        line.addSection(upStation, downStation, distance, duration);
        return line;
    }
}
