package nextstep.subway.unit;

import nextstep.subway.domain.Distance;
import nextstep.subway.domain.Duration;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {
    @Test
    void addSection() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(10), Duration.from(10)));
        line.addSection(createSectionBuilder(역삼역, 삼성역, Distance.from(5), Duration.from(5)));

        assertThat(line.getStations()).containsExactly(강남역, 역삼역, 삼성역);
    }

    @DisplayName("상행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(10), Duration.from(10)));
        line.addSection(createSectionBuilder(강남역, 삼성역, Distance.from(5), Duration.from(5)));

        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(5);
    }

    @DisplayName("상행 기준으로 목록 중간에 추가할 때 중간 구간의 소요시간이 초과할 경우 예외")
    @Test
    void addSectionInMiddleOverDuration() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(10), Duration.from(10)));

        assertThatIllegalArgumentException().isThrownBy(() -> line.addSection(createSectionBuilder(강남역, 삼성역, Distance.from(5), Duration.from(15))))
                .withMessage("소요 시간은 0 이하일 수 없습니다. 입력된 시간 : " + -5);
    }

    @DisplayName("상행 기준으로 목록 중간에 추가할 때 중간 구간의 길이가 더 길 경우 예외")
    @Test
    void addSectionInMiddleOverDistance() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(10), Duration.from(10)));

        assertThatIllegalArgumentException().isThrownBy(() -> line.addSection(createSectionBuilder(강남역, 삼성역, Distance.from(15), Duration.from(4))))
                .withMessage("거리는 0 이하가 될 수 없습니다. 입력된 거리 : "+ -5);
    }

    @DisplayName("하행 기준으로 목록 중간에 추가할 경우")
    @Test
    void addSectionInMiddle2() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(10), Duration.from(10)));
        line.addSection(createSectionBuilder(삼성역, 역삼역, Distance.from(5), Duration.from(5)));

        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(5);
    }

    @DisplayName("목록 앞에 추가할 경우")
    @Test
    void addSectionInFront() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(10), Duration.from(10)));
        line.addSection(createSectionBuilder(삼성역, 강남역, Distance.from(5), Duration.from(5)));

        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 강남역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(역삼역);
        assertThat(section.getDistance()).isEqualTo(10);
    }

    @DisplayName("목록 뒤에 추가할 경우")
    @Test
    void addSectionBehind() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");

        line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(10), Duration.from(10)));
        line.addSection(createSectionBuilder(역삼역, 삼성역, Distance.from(5), Duration.from(5)));

        assertThat(line.getSections().size()).isEqualTo(2);
        Section section = line.getSections().stream()
                .filter(it -> it.getUpStation() == 역삼역)
                .findFirst().orElseThrow(RuntimeException::new);
        assertThat(section.getDownStation()).isEqualTo(삼성역);
        assertThat(section.getDistance()).isEqualTo(5);
    }

    @Test
    void getStations() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");
        line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(10), Duration.from(10)));
        line.addSection(createSectionBuilder(강남역, 삼성역, Distance.from(5), Duration.from(5)));

        List<Station> result = line.getStations();

        assertThat(result).containsExactly(강남역, 삼성역, 역삼역);
    }

    @DisplayName("이미 존재하는 구간 추가 시 에러 발생")
    @Test
    void addSectionAlreadyIncluded() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Line line = new Line("2호선", "green");
        line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(10), Duration.from(10)));

        assertThatThrownBy(() -> line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(5), Duration.from(5))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void removeSection() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");
        line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(10), Duration.from(10)));
        line.addSection(createSectionBuilder(역삼역, 삼성역, Distance.from(5), Duration.from(5)));

        line.deleteSection(삼성역);

        assertThat(line.getStations()).containsExactly(강남역, 역삼역);
    }

    @Test
    void removeSectionInFront() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");
        line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(10), Duration.from(10)));
        line.addSection(createSectionBuilder(역삼역, 삼성역, Distance.from(5), Duration.from(5)));

        line.deleteSection(강남역);

        assertThat(line.getStations()).containsExactly(역삼역, 삼성역);
    }

    @Test
    void removeSectionInMiddle() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 삼성역 = new Station("삼성역");
        Line line = new Line("2호선", "green");
        line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(10), Duration.from(10)));
        line.addSection(createSectionBuilder(역삼역, 삼성역, Distance.from(5), Duration.from(5)));

        line.deleteSection(역삼역);

        assertThat(line.getStations()).containsExactly(강남역, 삼성역);
    }

    @DisplayName("구간이 하나인 노선에서 역 삭제 시 에러 발생")
    @Test
    void removeSectionNotEndOfList() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Line line = new Line("2호선", "green");
        line.addSection(createSectionBuilder(강남역, 역삼역, Distance.from(10), Duration.from(10)));

        assertThatThrownBy(() -> line.deleteSection(역삼역))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Line.SectionBuilder createSectionBuilder(Station upStation, Station downStation, Distance distance, Duration duration) {
        return new Line.SectionBuilder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .duration(duration)
                .build();
    }
}
