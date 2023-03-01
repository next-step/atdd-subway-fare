package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.fare.LineFarePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineFarePolicyTest {
    public static final int CHEAP_FARE = 900;
    public static final int EXPENSIVE_FARE = 901;

    private LineFarePolicy lineFarePolicy;

    private Path path;
    private Sections sections;

    private Line lineA;
    private Line lineB;

    private Section sectionA;
    private Section sectionB;

    private Station stationA;
    private Station stationB;
    private Station stationC;

    @BeforeEach
    void setUp() {
        lineFarePolicy = new LineFarePolicy();

        lineA = new Line("lineA", "lineA-color", CHEAP_FARE);
        lineB = new Line("lineB", "lineB-color", EXPENSIVE_FARE);

        stationA = new Station("stationA");
        stationB = new Station("stationB");
        stationC = new Station("stationC");

        sectionA = new Section(lineA, stationA, stationB, 5, 5);
        sectionB = new Section(lineB, stationB, stationC, 3, 3);

        sections = new Sections(List.of(sectionA, sectionB));

        path = new Path(sections);
    }

    @DisplayName("지나는 노선 중 추가 요금이 가장 비싼 요금을 반환한다.")
    @Test
    void getFare() {
        final int fare = lineFarePolicy.getFare(path);

        assertThat(fare).isEqualTo(EXPENSIVE_FARE);
    }
}
