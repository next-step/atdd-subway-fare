package nextstep.subway.unit;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.Sections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathTest {

    private static final int DEFAULT_FEE = 1250;

    @Mock
    private Sections sections;

    @DisplayName("거리가 10km 이내일 경우 기본운임을 부과한다")
    @Test
    void 거리가_10km_이내일_경우_기본운임을_부과한다() {
        // given
        when(sections.totalDistance()).thenReturn(10);

        // when
        Path path = Path.of(sections);

        // then
        assertThat(path.getFare()).isEqualTo(DEFAULT_FEE);
    }


    /**
     * 10km초과∼50km까지(5km마다 100원)
     */
    @DisplayName("거리가 20km일경우 200원의 추가요금이 포함된다")
    @Test
    void 거리가_20km일경우_200원의_추가요금이_포함된다() {
        // given
        when(sections.totalDistance()).thenReturn(20);

        // when
        Path path = Path.of(sections);

        // then
        assertThat(path.getFare()).isEqualTo(DEFAULT_FEE + 200);
    }

    /**
     * 50km초과 시 (8km마다 100원)
     */
    @DisplayName("거리가 60km일경우 700원의 추가요금이 포함된다")
    @Test
    void 거리가_60km일경우_700원의_추가요금이_포함된다() {
        // given
        when(sections.totalDistance()).thenReturn(60);

        // when
        Path path = Path.of(sections);

        // then
        assertThat(path.getFare()).isEqualTo(DEFAULT_FEE + 700);
    }
}
