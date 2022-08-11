package nextstep.subway.unit;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {

    @ParameterizedTest
    @CsvSource({"10, 1250", "12, 1350", "16, 1450", "50, 2050", "52, 2150", "62, 2250", "70, 2350"})
    void 요금계산(final int distance, final int farce) {
        final Path path = new Path(new Sections(List.of(getSection(distance))));

        assertThat(path.extractFare()).isEqualTo(farce);
    }

    private Section getSection(final int distance) {
        return new Section(null, null, null, distance, 50);
    }

}
