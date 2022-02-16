package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class LineServiceTest extends ServiceTestFixture {
    @Autowired
    private LineService lineService;

    @Test
    void saveLine() {
        // when
        LineResponse lineResponse = lineService.saveLine(
                new LineRequest("신분당선2", "red", 양재역.getId(), 판교역.getId(), 10, 6));

        // then
        Line line = lineService.findById(lineResponse.getId());
        assertThat(line.getSections().get(0).getDistance()).isEqualTo(10);
        assertThat(line.getSections().get(0).getDuration()).isEqualTo(6);
    }

    @Test
    void addSection() {
        // when
        lineService.addSection(신분당선.getId(), new SectionRequest(양재역.getId(), 판교역.getId(), 10, 6));

        // then
        Line line = lineService.findById(신분당선.getId());

        assertThat(line.getSections().size()).isEqualTo(2);
        assertThat(line.getSections().get(1).getDistance()).isEqualTo(10);
        assertThat(line.getSections().get(1).getDuration()).isEqualTo(6);
    }
}
