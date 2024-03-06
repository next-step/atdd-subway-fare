package nextstep.line;

import nextstep.section.SectionRequest;
import nextstep.station.Station;
import nextstep.station.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class LineServiceTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 선릉역;

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");

        stationRepository.saveAll(List.of(강남역, 역삼역, 선릉역));
    }

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void addSection() {
        Line 이호선 = new Line("2호선", "green", 강남역, 역삼역, 10L);
        lineRepository.save(이호선);

        SectionRequest sectionRequest = new SectionRequest(역삼역.getId(), 선릉역.getId(), 10L);
        lineService.addSection(이호선.getId(), sectionRequest);

        LineResponse lineResponse = lineService.findLineById(이호선.getId());
        assertThat(lineResponse.getStations()).hasSize(3);
    }

    @DisplayName("지하철 노선의 지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        Line 이호선 = new Line("2호선", "green", 강남역, 역삼역, 10L);
        lineRepository.save(이호선);

        LineResponse lineResponse = lineService.findLineById(이호선.getId());

        assertThat(lineResponse.getStations()).hasSize(2);
    }

    @DisplayName("지하철 노선에 구간을 삭제한다.")
    @Test
    void removeSection() {
        Line 이호선 = new Line("2호선", "green", 강남역, 역삼역, 10L);
        lineRepository.save(이호선);

        SectionRequest sectionRequest = new SectionRequest(역삼역.getId(), 선릉역.getId(), 10L);
        lineService.addSection(이호선.getId(), sectionRequest);
        lineService.deleteSection(이호선.getId(), 선릉역.getId());

        LineResponse lineResponse = lineService.findLineById(이호선.getId());
        assertThat(lineResponse.getStations()).hasSize(2);
    }
}