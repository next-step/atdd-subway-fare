package nextstep.subway.unit;

import nextstep.common.exception.NoDeleteOneSectionException;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.common.error.SubwayError.NO_DELETE_ONE_SECTION;
import static nextstep.subway.acceptance.StationSteps.역_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("노선 기능 테스트")
class LineTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;

    @BeforeEach
    void setUp() {
        강남역 = 역_생성(1L, "강남역");
        역삼역 = 역_생성(2L, "역삼역");
        삼성역 = 역_생성(3L, "삼성역");
    }

    @DisplayName("하행 기준으로 목록 끝에 추가할 경우")
    @Test
    void addSection() {
        Line line = new Line("2호선", "green");

        line.addSection(강남역, 역삼역, 10, 20);
        line.addSection(역삼역, 삼성역, 5, 10);

        assertThat(line.getStations()).containsExactly(강남역, 역삼역, 삼성역);
    }

    @DisplayName("상행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle() {
        Line line = new Line("2호선", "green");

        line.addSection(강남역, 역삼역, 10, 20);
        line.addSection(강남역, 삼성역, 5, 10);

        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertAll(
                () -> assertThat(line.getSections()).hasSize(2),
                () -> assertThat(section.getDownStation()).isEqualTo(삼성역),
                () -> assertThat(section.getDistance()).isEqualTo(5)
        );
    }

    @DisplayName("하행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle2() {
        Line line = new Line("2호선", "green");

        line.addSection(강남역, 역삼역, 10, 20);
        line.addSection(삼성역, 역삼역, 5, 5);

        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertAll(
                () -> assertThat(line.getSections()).hasSize(2),
                () -> assertThat(section.getDownStation()).isEqualTo(삼성역),
                () -> assertThat(section.getDistance()).isEqualTo(5)
        );
    }

    @DisplayName("목록 앞에 추가할 경우")
    @Test
    void addSectionInFront() {
        Line line = new Line("2호선", "green");

        line.addSection(강남역, 역삼역, 10, 20);
        line.addSection(삼성역, 강남역, 5, 10);

        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertAll(
                () -> assertThat(line.getSections()).hasSize(2),
                () -> assertThat(section.getDownStation()).isEqualTo(역삼역),
                () -> assertThat(section.getDistance()).isEqualTo(10)
        );
    }

    @DisplayName("목록 뒤에 추가할 경우")
    @Test
    void addSectionBehind() {
        Line line = new Line("2호선", "green");

        line.addSection(강남역, 역삼역, 10, 20);
        line.addSection(역삼역, 삼성역, 5, 10);

        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 역삼역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertAll(
                () -> assertThat(line.getSections()).hasSize(2),
                () -> assertThat(section.getDownStation()).isEqualTo(삼성역),
                () -> assertThat(section.getDistance()).isEqualTo(5)
        );
    }

    @DisplayName("역 조회")
    @Test
    void getStations() {
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 20);
        line.addSection(강남역, 삼성역, 5, 10);

        List<Station> result = line.getStations();

        assertThat(result).containsExactly(강남역, 삼성역, 역삼역);
    }

    @DisplayName("이미 존재하는 구간 추가 시 에러 발생")
    @Test
    void addSectionAlreadyIncluded() {
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 20);

        assertThatThrownBy(() -> line.addSection(강남역, 역삼역, 5, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("구간의 역 삭제")
    @Test
    void removeSection() {
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 20);
        line.addSection(역삼역, 삼성역, 5, 10);

        line.deleteSection(삼성역);

        assertThat(line.getStations()).containsExactly(강남역, 역삼역);
    }

    @DisplayName("상행종점역 삭제")
    @Test
    void removeSectionInFront() {
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 20);
        line.addSection(역삼역, 삼성역, 5, 10);

        line.deleteSection(강남역);

        assertThat(line.getStations()).containsExactly(역삼역, 삼성역);
    }

    @DisplayName("구간의 중간역 삭제")
    @Test
    void removeSectionInMiddle() {
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 20);
        line.addSection(역삼역, 삼성역, 5, 10);

        line.deleteSection(역삼역);

        assertThat(line.getStations()).containsExactly(강남역, 삼성역);
    }

    @DisplayName("구간이 하나인 노선에서 역 삭제 시 에러 발생")
    @Test
    void removeSectionNotEndOfList() {
        Line line = new Line("2호선", "green");
        line.addSection(강남역, 역삼역, 10, 20);

        assertThatThrownBy(() -> line.deleteSection(역삼역))
                .isInstanceOf(NoDeleteOneSectionException.class)
                .hasMessage(NO_DELETE_ONE_SECTION.getMessage());
    }
}
