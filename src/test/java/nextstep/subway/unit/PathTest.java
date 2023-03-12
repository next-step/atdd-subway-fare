package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTest {
    Line line;
    Station station1;
    Station station2;
    Station station3;
    Station station4;
    Section section1;
    Section section2;
    Section section3;
    Sections sections1;
    Sections sections2;
    Sections sections3;

    public void setUp() {
        line = new Line("line", "color");

        station1 = new Station("1");
        station2 = new Station("2");
        station3 = new Station("3");
        station4 = new Station("4");

        section1 = new Section(line, station1, station2, 5, 5);
        section2 = new Section(line, station2, station3, 10, 5);
        section3 = new Section(line, station3, station4, 43, 5);

        sections1 = new Sections(List.of(section1));
        sections2 = new Sections(List.of(section1, section2));
        sections3 = new Sections(List.of(section1, section2, section3));
    }

    @DisplayName("요금을 구한다 (10km 이내)")
    @Test
    void test1() {
        Path path = new Path(sections1);
        assertThat(path.extractFare()).isEqualTo(1250);
    }

    @DisplayName("요금을 구한다 (10km 이내)")
    @Test
    void test2() {
        Path path = new Path(sections1);
        assertThat(path.extractFare()).isEqualTo(1350);
    }

    @DisplayName("요금을 구한다 (10km 이내)")
    @Test
    void test3() {
        Path path = new Path(sections1);
        assertThat(path.extractFare()).isEqualTo(1850);
    }

}
