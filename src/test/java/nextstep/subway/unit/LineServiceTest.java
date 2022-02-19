package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LineServiceTest {
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    private Station 강남역;
    private Station 양재역;
    private Station 판교역;
    private Line 신분당선;


    @BeforeEach
    public void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        판교역 = stationRepository.save(new Station("판교역"));
        신분당선 = lineRepository.save(createLine("신분당선", "red", 강남역, 양재역, 5, 4));
    }
    
    @Test
    void addSection() {
        // when
        lineService.addSection(신분당선.getId(), new SectionRequest(양재역.getId(), 판교역.getId(), 10, 6));

        // then
        Line line = lineService.findById(신분당선.getId());

        assertThat(line.getSections().size()).isEqualTo(2);
    }

    private Line createLine(String name, String color, Station upStation, Station downStation, int distance, int duration) {
        Line line = new Line(name, color);
        line.addSection(upStation, downStation, distance, duration);
        return line;
    }
}
