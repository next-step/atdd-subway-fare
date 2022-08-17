package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;
    private Station 교대역;
    private Station 신사역;
    private Line 이호선;
    private Line 삼호선;
    private Section 강남_역삼;
    private Section 강남_삼성;
    private Section 역삼_삼성;
    private Section 삼성_역삼;
    private Section 강남_교대;
    private Section 교대_신사;

    static void 라인에_구간_등록(Line line, Section section) {
        line.addSection(section.getUpStation(), section.getDownStation(), section.getDistance(),
                section.getDuration());
    }

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        삼성역 = new Station("삼성역");
        교대역 = new Station("교대역");
        신사역 = new Station("신사역");

        이호선 = new Line("2호선", "green", 100);
        삼호선 = new Line("3호선 ", "orange", 200);

        강남_역삼 = new Section(이호선, 강남역, 역삼역, 10, 10);
        강남_삼성 = new Section(이호선, 강남역, 삼성역, 5, 15);
        역삼_삼성 = new Section(이호선, 역삼역, 삼성역, 5, 5);
        삼성_역삼 = new Section(이호선, 삼성역, 역삼역, 5, 5);

        강남_교대 = new Section(이호선, 강남역, 교대역, 3, 3);
        교대_신사 = new Section(삼호선, 교대역, 강남역, 4, 4);


    }

    @DisplayName("요금 정책 리팩토링을 위한 노선별 추가요금 확인")
    @Test
    void checkExtraCharge() {

        assertThat(이호선.getExtraCharge()).isEqualTo(100);
        assertThat(삼호선.getExtraCharge()).isEqualTo(200);
        assertThat(이호선.getExtraCharge()).isLessThan(삼호선.getExtraCharge());
    }

    @DisplayName("노선별 추가 요금중 최대 값 구하기")
    @Test
    void getMexExtraCharge() {
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(강남_교대);
        sectionList.add(교대_신사);
        Sections sections = new Sections(sectionList);

        assertThat(sections.getMaxExtraCharge()).isEqualTo(200);
    }

    @Test
    void addSection() {

        라인에_구간_등록(이호선, 강남_역삼);
        라인에_구간_등록(이호선, 역삼_삼성);
        assertThat(이호선.getStations()).containsExactly(강남역, 역삼역, 삼성역);
    }

    @DisplayName("상행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle() {


        라인에_구간_등록(이호선, 강남_역삼);
        라인에_구간_등록(이호선, 강남_삼성);

        assertThat(이호선.getSections().size()).isEqualTo(2);
        Section section = 이호선.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(5);
    }

    @DisplayName("하행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle2() {

        라인에_구간_등록(이호선, 강남_역삼);
        라인에_구간_등록(이호선, 삼성_역삼);

        assertThat(이호선.getSections().size()).isEqualTo(2);
        Section section = 이호선.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(5);
    }

    @DisplayName("목록 앞에 추가할 경우")
    @Test
    void addSectionInFront() {

        라인에_구간_등록(이호선, 강남_역삼);
        라인에_구간_등록(이호선, 역삼_삼성);

        assertThat(이호선.getSections().size()).isEqualTo(2);
        Section section = 이호선.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(역삼역);
        assertThat(section.getDistance()).isEqualTo(10);
    }

    @DisplayName("목록 뒤에 추가할 경우")
    @Test
    void addSectionBehind() {
        라인에_구간_등록(이호선, 강남_역삼);
        라인에_구간_등록(이호선, 역삼_삼성);

        assertThat(이호선.getSections().size()).isEqualTo(2);
        Section section = 이호선.getSections().stream()
                .filter(it -> it.getUpStation() == 역삼역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(5);
    }

    @Test
    void getStations() {

        라인에_구간_등록(이호선, 강남_역삼);
        라인에_구간_등록(이호선, 강남_삼성);

        List<Station> result = 이호선.getStations();

        assertThat(result).containsExactly(강남역, 삼성역, 역삼역);
    }

    @DisplayName("이미 존재하는 구간 추가 시 에러 발생")
    @Test
    void addSectionAlreadyIncluded() {

        라인에_구간_등록(이호선, 강남_역삼);

        assertThatThrownBy(() -> 라인에_구간_등록(이호선, 강남_역삼))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void removeSection() {

        라인에_구간_등록(이호선, 강남_역삼);
        라인에_구간_등록(이호선, 역삼_삼성);

        이호선.deleteSection(삼성역);

        assertThat(이호선.getStations()).containsExactly(강남역, 역삼역);
    }

    @Test
    void removeSectionInFront() {

        라인에_구간_등록(이호선, 강남_역삼);
        라인에_구간_등록(이호선, 역삼_삼성);

        이호선.deleteSection(강남역);

        assertThat(이호선.getStations()).containsExactly(역삼역, 삼성역);
    }

    @Test
    void removeSectionInMiddle() {

        라인에_구간_등록(이호선, 강남_역삼);
        라인에_구간_등록(이호선, 역삼_삼성);

        이호선.deleteSection(역삼역);

        assertThat(이호선.getStations()).containsExactly(강남역, 삼성역);
    }

    @DisplayName("구간이 하나인 노선에서 역 삭제 시 에러 발생")
    @Test
    void removeSectionNotEndOfList() {

        라인에_구간_등록(이호선, 강남_역삼);

        assertThatThrownBy(() -> 이호선.deleteSection(역삼역))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
