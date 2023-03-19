package nextstep.subway.unit.support;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.Path;
import nextstep.subway.fixture.SectionFixture;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LineSupporter {

    public static void 역이_순서대로_정렬되어_있다(Line 호선, Station... 역_목록) {
        assertThat(호선.getStations()).containsExactly(역_목록);
    }

    public static void 역이_순서대로_정렬되어_있다(Path 경로, Station... 역_목록) {
        assertThat(경로.getStations()).containsExactly(역_목록);
    }

    public static void 구간_거리가_순서대로_저장되어_있다(List<Section> 구간_목록, Integer... 거리_목록) {
        assertThat(구간_목록.stream()
                .map(Section::getDistance)
                .collect(Collectors.toList())
        ).containsExactly(거리_목록);
    }

    public static void 구간_소요시간이_순서대로_저장되어_있다(List<Section> 구간_목록, Integer... 소요_시간_목록) {
        assertThat(구간_목록.stream()
                .map(Section::getDuration)
                .collect(Collectors.toList())
        ).containsExactly(소요_시간_목록);
    }

    public static void 구간이_N개_저장되어_있다(Line 노선, int 구간_개수) {
        assertThat(노선.getSections()).hasSize(구간_개수);
    }

    public static void 구간의_상행역_하행역_정보가_일치한다(Line 노선, Station 상행역, Station 하행역) {
        Section section = 노선.getSections().stream()
                .filter(it -> it.getUpStation() == 상행역)
                .findFirst().orElseThrow(RuntimeException::new);

        assertThat(section.getDownStation()).isEqualTo(하행역);
    }

    public static void 구간_거리와_소요시간_정보가_일치한다(Line 노선, Station 상행역, SectionFixture 상행_하행_구간) {
        Section section = 노선.getSections().stream()
                .filter(it -> it.getUpStation() == 상행역)
                .findFirst().orElseThrow(RuntimeException::new);

        assertAll(
                () -> assertThat(section.getDistance()).isEqualTo(상행_하행_구간.구간_거리()),
                () -> assertThat(section.getDuration()).isEqualTo(상행_하행_구간.구간_소요시간())
        );
    }

    public static void 구간_거리와_소요시간_정보가_일치한다(Line 노선, Station 상행역, int 구간_거리, int 구간_소요시간) {
        Section section = 노선.getSections().stream()
                .filter(it -> it.getUpStation() == 상행역)
                .findFirst().orElseThrow(RuntimeException::new);

        assertAll(
                () -> assertThat(section.getDistance()).isEqualTo(구간_거리),
                () -> assertThat(section.getDuration()).isEqualTo(구간_소요시간)
        );
    }
}
