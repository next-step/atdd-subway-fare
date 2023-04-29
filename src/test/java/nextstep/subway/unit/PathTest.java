package nextstep.subway.unit;

import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
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

    Line lineExtraFee;
    Station station5;
    Station station6;
    Section section4;
    Section section5;
    Sections sections4;
    MemberResponse member13;
    MemberResponse member6;


    @BeforeEach
    public void setUp() {
        line = new Line("line", "color", 0);

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

        lineExtraFee = new Line("line", "color", 1000);

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


        line = new Line("line2", "color2", 1000);

        station4 = new Station("1");
        station5 = new Station("2");
        station6 = new Station("3");

        section4 = new Section(line, station4, station5, 5, 5);
        section5 = new Section(line, station5, station6, 5, 5);

        sections4 = new Sections(List.of(section4, section5));

        member13 = new MemberResponse(1L, "email", 13);
        member6 = new MemberResponse(2L, "email2", 6);
    }

    @DisplayName("요금을 구한다 (10km 이내)")
    @Test
    void test1() {
        Path path = new Path(sections1);
        assertThat(path.extractFare(null)).isEqualTo(1250);
    }

    @DisplayName("요금을 구한다 (10km 초과 ~ 50km 까지)")
    @Test
    void test2() {
        Path path = new Path(sections2);
        assertThat(path.extractFare(null)).isEqualTo(1350);
    }

    @DisplayName("요금을 구한다 (50km 초과)")
    @Test
    void test3() {
        Path path = new Path(sections3);
        assertThat(path.extractFare(null)).isEqualTo(2150);
    }

    @DisplayName("요금을 구한다 (라인 추가요금)")
    @Test
    void test4() {
        Path path = new Path(sections4);
        assertThat(path.extractFare(null)).isEqualTo(2250);
    }

    @DisplayName("요금을 구한다 (13세 청소년 할인)")
    @Test
    void test5() {
        Path path = new Path(sections1);
        assertThat(path.extractFare(member13)).isEqualTo(1070);
    }

    @DisplayName("요금을 구한다 (6세 청소년 할인)")
    @Test
    void test6() {
        Path path = new Path(sections1);
        assertThat(path.extractFare(member6)).isEqualTo(800);
    }

    @DisplayName("복합 요금을 구한다. (10km 이내, 라인 추가요금, 13세 청소년 할인")
    @Test
    void test7() {
        Path path = new Path(sections4);
        assertThat(path.extractFare(member13)).isEqualTo(1870);
    }

}
