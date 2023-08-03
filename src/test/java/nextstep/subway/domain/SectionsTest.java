package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private Line line;
    private Station 부평역;
    private Station 부천역;
    private Station 개봉역;
    private Station 구로역;


    @BeforeEach
    public void setUp() {
        line = mock(Line.class);
        부평역 = mock(Station.class);
        부천역 = mock(Station.class);
        개봉역 = mock(Station.class);
        구로역 = mock(Station.class);
    }

    @Test
    @DisplayName("신규 구간의 하행역과 기존구간의 하행역에 대한 구간을 추가")
    void rearrangeSectionWithUpStation() {

        List<Section> sectionList = new ArrayList<>();
        sectionList.add(new Section(line, 부평역, 부천역, 10, 200));
        sectionList.add(new Section(line, 부천역, 구로역, 16, 600));

        //given
        Sections sections = new Sections(sectionList);

        //----부평 부천 (개봉) 구로
        //when
        sections.add(new Section(line, 부천역, 개봉역, 7, 350));

        //then
        assertThat(sections.getStations()).containsExactly(부평역, 부천역, 개봉역, 구로역);
        assertThat(sections.getSections()).extracting("distance").containsOnly(10, 7, 9);
        assertThat(sections.getSections()).extracting("duration").containsOnly(200, 250, 350);

    }

    @Test
    @DisplayName("신규 구간의 상행역과 기존 구간의 상행역에 대한 구간을 추가")
    void rearrangeSectionWithDownStation() {

        List<Section> sectionList = new ArrayList<>();
        sectionList.add(new Section(line, 부평역, 부천역, 10, 200));
        sectionList.add(new Section(line, 부천역, 구로역, 16, 600));

        //given
        Sections sections = new Sections(sectionList);

        //----부평 부천 (개봉) 구로
        //when
        sections.add(new Section(line, 개봉역, 구로역, 7, 350));

        //then
        assertThat(sections.getStations()).containsExactly(부평역, 부천역, 개봉역, 구로역);
        assertThat(sections.getSections()).extracting("distance").containsOnly(10, 7, 9);
        assertThat(sections.getSections()).extracting("duration").containsOnly(200, 250, 350);

    }

    @Test
    @DisplayName("구간 삭제 - 삭제하는 구간의 상행, 하행 구간이 존재할 경우 두 구간을 연결")
    void deleteMiddleSection() {

        List<Section> sectionList = new ArrayList<>();
        sectionList.add(new Section(line, 부평역, 부천역, 10, 200));
        sectionList.add(new Section(line, 부천역, 개봉역, 10, 300));
        sectionList.add(new Section(line, 개봉역, 구로역, 16, 600));

        //given
        Sections sections = new Sections(sectionList);

        //----부평 부천 (개봉) 구로
        //when
        sections.delete(개봉역);

        //then
        assertThat(sections.getStations()).containsExactly(부평역, 부천역, 구로역);
        assertThat(sections.getSections()).extracting("distance").containsOnly(10, 26);

    }
}