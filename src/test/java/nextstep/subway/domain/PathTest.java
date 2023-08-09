package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.when;

import java.util.List;
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

    @Test
    @DisplayName("추가 요금이 있는 노선을 이용시 요금 조회")
    void getOverFareThenLine() {

        Line line1 = mock(Line.class);
        List<Line> lines = List.of(line1);

        when(path.getLines()).thenReturn(lines);
        when(path.extractDistance()).thenReturn(8);
        when(line1.getUsageFee()).thenReturn(800);

        assertThat(path.getFare()).isEqualTo(DEFAULT_FARE + 800);
    }

    @Test
    @DisplayName("추가 요금이 있는 노선이 여러개일 경우 가장 높은 금액의 노선 요금만 적용")
    void getOverFareThenLineOnlyOne() {

        Line line1 = mock(Line.class);
        Line line2 = mock(Line.class);
        List<Line> lines = List.of(line1, line2);

        when(path.getLines()).thenReturn(lines);
        when(path.extractDistance()).thenReturn(8);

        when(line1.getUsageFee()).thenReturn(500);
        when(line2.getUsageFee()).thenReturn(300);

        assertThat(path.getFare()).isEqualTo(DEFAULT_FARE + 500);
    }
}