package nextstep.subway.line.service;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.repository.LineRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.repository.StationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class LineServiceTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @Test
    void addSection() {
        // given
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 역삼역 = stationRepository.save(new Station("역삼역"));
        Station 삼성역 = stationRepository.save(new Station("삼성역"));
        Line 이호선 = lineRepository.save(createLine(강남역, 삼성역));

        // when
        lineService.addSection(이호선.getId(), new SectionRequest(강남역.getId(), 역삼역.getId(), 4, 4));

        // then
        Line line = lineService.findById(이호선.getId());
        List<Section> result = line.getSections();
        Section section = result.stream()
                .filter(it -> it.getUpStation() == 역삼역)
                .findFirst()
                .orElseThrow(RuntimeException::new);

        assertAll(() -> {
            assertThat(result.size()).isEqualTo(2);
            assertThat(section.getDistance()).isEqualTo(6);
            assertThat(section.getDuration()).isEqualTo(6);
        });
    }

    private Line createLine(Station 강남역, Station 삼성역) {
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 삼성역, 10, 10);
        return line;
    }
}
