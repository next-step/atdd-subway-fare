package nextstep.subway.unit.map;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.map.TransferLine;
import nextstep.subway.domain.map.TransferLineBuilder;

@DisplayName("TransferLineBuilder Test")
public class TransferLineBuilderTest {
    @DisplayName("Sections를 환승 시점에 맞춰 분리 해준다.")
    @Test
    void createTest() {
        // Given
        Line 첫번째_라인 = new Line();
        Line 두번째_라인 = new Line();
        List<Section> sections = Arrays.asList(
            new Section(첫번째_라인, null, null, 0, 0),
            new Section(첫번째_라인, null, null, 0, 0),
            new Section(두번째_라인, null, null, 0, 0),
            new Section(첫번째_라인, null, null, 0, 0)
        );

        // When
        List<TransferLine> lineTransfers =
            TransferLineBuilder.create(new Sections(sections));

        // Then
        assertThat(lineTransfers.size()).isEqualTo(3);
    }
}
