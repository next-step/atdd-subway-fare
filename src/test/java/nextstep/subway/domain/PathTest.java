package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    private static final int DEFAULT_FARE = 1250;
    private Path path;

    @BeforeEach
    void setUp() {
        path = mock(Path.class);
        when(path.getFare()).thenCallRealMethod();
    }

    @Test
    @DisplayName("거리에 따른 추가 운임요금 테스트 (기본요금)")
    void getDefaultFare() {

        when(path.extractDistance()).thenReturn(10);
        assertThat(path.getFare()).isEqualTo(DEFAULT_FARE);
    }

    @Test
    @DisplayName("거리에 따른 추가 운임요금 테스트 (15km)")
    void getOverFare() {

        when(path.extractDistance()).thenReturn(15);
        assertThat(path.getFare()).isEqualTo(DEFAULT_FARE + 100);
    }
}