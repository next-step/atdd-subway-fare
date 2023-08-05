package nextstep.subway.unit;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.Sections;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathTest {

    @Mock
    private Sections sections;

    private Path path;

    @BeforeEach
    void setUp() {
        path = new Path(sections);
    }

    @Test
    void 거리_10km_이하_요금_계산() {
        //when
        when(sections.totalDistance()).thenReturn(10);

        //then
        Assertions.assertThat(path.getFee()).isEqualTo(1250);
    }

    @Test
    void 거리_10km_초과_50km_까지_요금_계산() {
        //when
        when(sections.totalDistance()).thenReturn(16);

        //then
        Assertions.assertThat(path.getFee()).isEqualTo(1450);
    }

    @Test
    void 거리_50km_초과_요금_계산() {
        //when
        when(sections.totalDistance()).thenReturn(58);

        //then
        Assertions.assertThat(path.getFee()).isEqualTo(2150);
    }
}
