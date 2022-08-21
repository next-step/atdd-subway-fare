package nextstep.subway.unit;

import nextstep.subway.domain.*;
import nextstep.subway.domain.fare.LineSurchargeFarePolicy;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineSurchargeFarePolicyTest {

    private Line 신분당선;
    private Line 분당선;
    private Station 판교역;
    private Station 미금역;
    private Station 정자역;
    private Station 수내역;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red", 900);
        분당선 = new Line("분당선", "yellow", 300);
        판교역 = new Station("판교역");
        미금역 = new Station("미금역");
        정자역 = new Station("정자역");
        수내역 = new Station("수내역");
    }

    @Test
    void addMaxLineSurcharge() {
        Sections sections = new Sections(Lists.newArrayList(
                new Section(신분당선, 판교역, 미금역, 5, 1),
                new Section(신분당선, 미금역, 정자역, 4, 1),
                new Section(분당선, 정자역, 수내역, 3, 1)
        ));
        Path path = new Path(sections);
        LineSurchargeFarePolicy lineSurchargeFarePolicy = new LineSurchargeFarePolicy();

        var fare = lineSurchargeFarePolicy.fare(path);

        assertThat(fare).isEqualTo(신분당선.getSurcharge());
    }
}
