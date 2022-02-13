package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    private Station 역삼역;
    private Station 삼성역;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));
        삼성역 = stationRepository.save(new Station("삼성역"));
    }


    @Test
    void addSection() {
        Line 이호선 = lineRepository.save(createLine(강남역, 역삼역));

        lineService.addSection(이호선.getId(), new SectionRequest(역삼역.getId(), 삼성역.getId(), 10));

        Line line = lineService.findById(이호선.getId());

        assertThat(line.getSections().size()).isEqualTo(2);
    }

    @DisplayName("노선에 구간을 추가")
    @Test
    void addSection2() {
        // given
        int distance = 10;
        int duration = 20;
        Line 이호선 = lineRepository.save(createLine2(강남역, 역삼역, distance, duration));
        lineService.addSection(이호선.getId(), new SectionRequest(역삼역.getId(), 삼성역.getId(), distance, duration));

        // when
        Line line = lineService.findById(이호선.getId());

        // then
        Section section = line.getSections().get(0);
        assertThat(line.getSections().size()).isEqualTo(2);
        assertThat(section.getDistance()).isEqualTo(distance);
        assertThat(section.getDuration()).isEqualTo(duration);
    }

    private Line createLine2(Station 강남역, Station 역삼역, int distance, int duration) {
        Line line = new Line("2호선", "green");
        line.addSection2(강남역, 역삼역, distance, duration);
        return line;
    }

    private Line createLine(Station 강남역, Station 역삼역) {
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10);
        return line;
    }
}
