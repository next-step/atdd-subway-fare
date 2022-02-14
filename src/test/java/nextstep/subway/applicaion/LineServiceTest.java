package nextstep.subway.applicaion;

import nextstep.exception.station.StationNotFoundException;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.step.LineServiceSteps;
import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @DisplayName("노선을 추가")
    @Test
    void saveLine2() {
        // given
        String name = "노선이름";
        String color = "red";
        LineRequest 요청 = LineServiceSteps.노선_추가_요청_생성(name, color,
                강남역.getId(), 역삼역.getId(),
                100, 10);

        // when
        LineResponse response = lineService.saveLine(요청);

        // then
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getColor()).isEqualTo(color);
    }

    @DisplayName("노선에 없는 역윽 구간으로 추가 하면 실패")
    @Test
    void saveLine2_notFoundStation() {
        // given
        String name = "노선이름";
        String color = "red";
        LineRequest 요청 = LineServiceSteps.노선_추가_요청_생성(name, color,
                1000L, 1000L,
                100, 10);

        // then
        assertThatThrownBy(() -> lineService.saveLine(요청))
                .isInstanceOf(StationNotFoundException.class);
    }

    @DisplayName("노선에 구간을 추가")
    @Test
    void addSection() {
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
        line.addSection(강남역, 역삼역, distance, duration);
        return line;
    }

    private Line createLine(Station 강남역, Station 역삼역) {
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 10);
        return line;
    }

}