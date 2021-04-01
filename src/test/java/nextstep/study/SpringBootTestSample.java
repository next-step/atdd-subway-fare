package nextstep.study;

import nextstep.subway.SubwayApplication;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SubwayApplication.class)
@Transactional
public class SpringBootTestSample {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;

    private Line 이호선;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));
        삼성역 = stationRepository.save(new Station("삼성역"));

        이호선 = lineRepository.save(new Line("2호선", "green", 0));
    }

    @Test
    void addSection() {
        // given
        lineService.addSectionToLine(이호선.getId(), 구간_추가_요청(강남역, 역삼역, 10, 10));

        // when
        lineService.addSectionToLine(이호선.getId(), 구간_추가_요청(역삼역, 삼성역, 10, 10));

        // then
        Line line = lineService.findLineById(이호선.getId());
        assertThat(line.getStations().size()).isEqualTo(3);
        assertThat(line.getSections()).hasSize(2);
    }

    private SectionRequest 구간_추가_요청(Station upStation, Station downStation, int distance, int duration) {
        return new SectionRequest(upStation.getId(), downStation.getId(), distance, duration);
    }
}
