package nextstep.subway.path.domain;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FareTest {

    @Mock
    private PathResult pathResult;

    @ParameterizedTest(name = "최단 거리가 {0}km 일 경우 요금 조회")
    @CsvSource({"5, 1250", "10, 1250", "23, 1550", "50, 2050", "58, 2150", "70, 2350"})
    void getFareOf5km(int distance, int fare) {
        // given
        when(pathResult.getTotalDistance()).thenReturn(distance);

        // when
        int totalFare = Fare.getTotalFare(pathResult);

        // then
        assertThat(totalFare).isEqualTo(fare);
    }
}