package nextstep.subway.unit;

import nextstep.subway.domain.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    private Line 신분당선;
    private Station 광교역;
    private Station 광교중앙역;
    private Station 상현역;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "red");
        광교역 = new Station("광교역");
        광교중앙역 = new Station("광교중앙역");
        상현역 = new Station("상현역");
    }

    @Test
    void extractDuration() {
        var sections = new Sections(Lists.newArrayList(
                new Section(신분당선, 광교역, 광교중앙역, 10, 5),
                new Section(신분당선, 광교중앙역, 상현역, 5, 2)
        ));

        var path = new Path(sections);

        assertThat(path.extractDuration()).isEqualTo(7);
    }
}
