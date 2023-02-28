package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SectionsTest {

    @Test
    @DisplayName("노선의 상행역 앞에 구간을 추가한다.")
    void addUpStation() {
        // given
        Station 강남역 = new Station("강남역");
        Station 서초역 = new Station("서초역");
        int 강남_서초_거리 = 10;
        int 강남_서초_소요시간 = 5;
        Line 이호선 = new Line("2호선", "green");
        Section 강남_서초_구간 = new Section(이호선, 강남역, 서초역, 강남_서초_거리, 강남_서초_소요시간);
        Sections sections = new Sections(강남_서초_구간);

        // when
        Station 역삼역 = new Station("역삼역");
        int 역삼_강남_거리 = 5;
        int 역삼_강남_소요시간 = 3;
        Section 역삼_강남_구간 = new Section(이호선, 역삼역, 강남역, 역삼_강남_거리, 역삼_강남_소요시간);
        sections.add(역삼_강남_구간);

        // then
        List<Section> 이호선_구간들 = sections.getSections();
        assertThat(이호선_구간들.size()).isEqualTo(2);
        assertThat(역이_상행역인_구간_추출(이호선_구간들, 역삼역).getDuration()).isEqualTo(역삼_강남_소요시간);
        assertThat(역이_상행역인_구간_추출(이호선_구간들, 역삼역).getDistance()).isEqualTo(역삼_강남_거리);
    }

    @Test
    @DisplayName("노선의 하행역에 구간을 추가한다.")
    void addDownStation() {
        // given
        Station 강남역 = new Station("강남역");
        Station 서초역 = new Station("서초역");
        int 강남_서초_거리 = 10;
        int 강남_서초_소요시간 = 5;
        Line 이호선 = new Line("2호선", "green");
        Section 강남_서초_구간 = new Section(이호선, 강남역, 서초역, 강남_서초_거리, 강남_서초_소요시간);
        Sections sections = new Sections(강남_서초_구간);

        // when
        Station 방배역 = new Station("방배역");
        int 서초_방배_거리 = 5;
        int 서초_방배_소요시간 = 3;
        Section 서초_방배_구간 = new Section(이호선, 서초역, 방배역, 서초_방배_거리, 서초_방배_소요시간);
        sections.add(서초_방배_구간);

        // then
        List<Section> 이호선_구간들 = sections.getSections();
        assertThat(이호선_구간들.size()).isEqualTo(2);
        assertThat(역이_상행역인_구간_추출(이호선_구간들, 서초역).getDuration()).isEqualTo(서초_방배_소요시간);
        assertThat(역이_상행역인_구간_추출(이호선_구간들, 서초역).getDistance()).isEqualTo(서초_방배_거리);
    }

    private static Section 역이_상행역인_구간_추출(List<Section> sections, Station station) {
        return sections.stream()
                .filter(it -> Objects.equals(it.getUpStation(), station))
                .findFirst()
                .get();
    }

    @Test
    @DisplayName("노선의 중간에 구간을 추가한다.")
    void addMiddle() {
        // given
        Station 강남역 = new Station("강남역");
        Station 서초역 = new Station("서초역");
        int 강남_서초_거리 = 10;
        int 강남_서초_소요시간 = 5;
        Line 이호선 = new Line("2호선", "green");
        Section 강남_서초_구간 = new Section(이호선, 강남역, 서초역, 강남_서초_거리, 강남_서초_소요시간);
        Sections sections = new Sections(강남_서초_구간);

        // when
        Station 교대역 = new Station("교대역");
        int 강남_교대_거리 = 5;
        int 강남_교대_소요시간 = 3;
        Section 강남_교대_구간 = new Section(이호선, 강남역, 교대역, 강남_교대_거리, 강남_교대_소요시간);
        sections.add(강남_교대_구간);

        // then
        List<Section> 이호선_구간들 = sections.getSections();
        assertThat(이호선_구간들.size()).isEqualTo(2);
        assertThat(역이_상행역인_구간_추출(이호선_구간들, 교대역).getDuration()).isEqualTo(강남_서초_소요시간 - 강남_교대_소요시간);
        assertThat(역이_상행역인_구간_추출(이호선_구간들, 교대역).getDistance()).isEqualTo(강남_서초_거리 - 강남_교대_거리);
    }

    @Test
    @DisplayName("노선의 상행역 구간을 제거한다.")
    void deleteUpStation() {
        // given
        Station 강남역 = new Station("강남역");
        Station 서초역 = new Station("서초역");
        int 강남_서초_거리 = 10;
        int 강남_서초_소요시간 = 5;
        Line 이호선 = new Line("2호선", "green");
        Section 강남_서초_구간 = new Section(이호선, 강남역, 서초역, 강남_서초_거리, 강남_서초_소요시간);
        Sections sections = new Sections(강남_서초_구간);
        Station 역삼역 = new Station("역삼역");
        int 역삼_강남_거리 = 5;
        int 역삼_강남_소요시간 = 3;
        Section 역삼_강남_구간 = new Section(이호선, 역삼역, 강남역, 역삼_강남_거리, 역삼_강남_소요시간);
        sections.add(역삼_강남_구간);
        // 노선: 역삼 - 강남 - 서초

        // when
        sections.delete(역삼역);

        // then
        List<Section> 이호선_구간들 = sections.getSections();
        assertThat(이호선_구간들.size()).isEqualTo(1);
        assertThat(역이_상행역인_구간_추출(이호선_구간들, 강남역).getDuration()).isEqualTo(강남_서초_소요시간);
        assertThat(역이_상행역인_구간_추출(이호선_구간들, 강남역).getDistance()).isEqualTo(강남_서초_거리);
    }

    @Test
    @DisplayName("노선의 하행역 구간을 제거한다.")
    void deleteDownStation() {
        // given
        Station 강남역 = new Station("강남역");
        Station 서초역 = new Station("서초역");
        int 강남_서초_거리 = 10;
        int 강남_서초_소요시간 = 5;
        Line 이호선 = new Line("2호선", "green");
        Section 강남_서초_구간 = new Section(이호선, 강남역, 서초역, 강남_서초_거리, 강남_서초_소요시간);
        Sections sections = new Sections(강남_서초_구간);
        Station 역삼역 = new Station("역삼역");
        int 역삼_강남_거리 = 5;
        int 역삼_강남_소요시간 = 3;
        Section 역삼_강남_구간 = new Section(이호선, 역삼역, 강남역, 역삼_강남_거리, 역삼_강남_소요시간);
        sections.add(역삼_강남_구간);
        // 노선: 역삼 - 강남 - 서초

        // when
        sections.delete(서초역);

        // then
        List<Section> 이호선_구간들 = sections.getSections();
        assertThat(이호선_구간들.size()).isEqualTo(1);
        assertThat(역이_상행역인_구간_추출(이호선_구간들, 역삼역).getDuration()).isEqualTo(역삼_강남_소요시간);
        assertThat(역이_상행역인_구간_추출(이호선_구간들, 역삼역).getDistance()).isEqualTo(역삼_강남_거리);
    }

    @Test
    @DisplayName("노선의 중간 구간을 제거한다.")
    void deleteMiddleStation() {
        // given
        Station 강남역 = new Station("강남역");
        Station 서초역 = new Station("서초역");
        int 강남_서초_거리 = 10;
        int 강남_서초_소요시간 = 5;
        Line 이호선 = new Line("2호선", "green");
        Section 강남_서초_구간 = new Section(이호선, 강남역, 서초역, 강남_서초_거리, 강남_서초_소요시간);
        Sections sections = new Sections(강남_서초_구간);
        Station 역삼역 = new Station("역삼역");
        int 역삼_강남_거리 = 5;
        int 역삼_강남_소요시간 = 3;
        Section 역삼_강남_구간 = new Section(이호선, 역삼역, 강남역, 역삼_강남_거리, 역삼_강남_소요시간);
        sections.add(역삼_강남_구간);
        // 소요시간:  3     5
        // 거리:      5    10
        // 노선: 역삼 - 강남 - 서초

        // when
        sections.delete(강남역);
        int 역삼_서초_소요시간 = 8;
        int 역삼_서초_거리 = 15;

        // then
        List<Section> 이호선_구간들 = sections.getSections();
        assertThat(이호선_구간들.size()).isEqualTo(1);
        assertThat(역이_상행역인_구간_추출(이호선_구간들, 역삼역).getDuration()).isEqualTo(역삼_서초_소요시간);
        assertThat(역이_상행역인_구간_추출(이호선_구간들, 역삼역).getDistance()).isEqualTo(역삼_서초_거리);
    }
}