package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SectionsTest {
    Section 이호선_사당역_교대역_구간;
    Section 이호선_교대역_강남역_구간;
    Section 신분당선_강남역_판교역_구간;
    Section 신분당선_판교역_정자역_구간;
    Section 사호선_과천역_사당역_구간;

    @BeforeEach
    void setUp() {
        Line 이호선 = new Line("이호선", "green");
        Line 사호선 = new Line("사호선", "yellow", 500);
        Line 신분당선 = new Line("신분당선", "red", 900);

        이호선_사당역_교대역_구간 = new Section(이호선, new Station("사당역"), new Station("교대역"), 10, 5);
        이호선_교대역_강남역_구간 = new Section(이호선, new Station("교대역"), new Station("강남역"), 10, 5);
        사호선_과천역_사당역_구간 = new Section(사호선, new Station("과천역"), new Station("사당역"), 10, 5);
        신분당선_강남역_판교역_구간 = new Section(신분당선, new Station("강남역"), new Station("판교역"), 10, 5);
        신분당선_판교역_정자역_구간 = new Section(신분당선, new Station("판교역"), new Station("정자역"), 10, 5);
    }

    @Test
    void noOverFareLine(){
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(이호선_사당역_교대역_구간);
        sectionList.add(이호선_교대역_강남역_구간);

        Sections sections = new Sections(sectionList);

        assertThat(sections.overFareLine()).isEqualTo(0);
    }

    @Test
    void overFareLine(){
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(이호선_사당역_교대역_구간);
        sectionList.add(사호선_과천역_사당역_구간);

        Sections sections = new Sections(sectionList);

        assertThat(sections.overFareLine()).isEqualTo(500);
    }

    @Test
    void OnlyMaxOverFare(){
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(이호선_사당역_교대역_구간);
        sectionList.add(사호선_과천역_사당역_구간);
        sectionList.add(신분당선_판교역_정자역_구간);

        Sections sections = new Sections(sectionList);

        assertThat(sections.overFareLine()).isEqualTo(900);
    }
}
