package nextstep.subway.path;

import nextstep.subway.path.application.FareService;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FareServiceMockTest {
    @Mock
    private PathResult pathResult;

    private FareService fareService;

    @BeforeEach
    void setUp() {
        fareService = new FareService();
    }

    @Test
    void calculateFare() {
        when(pathResult.getTotalDistance()).thenReturn(10);
        assertThat(fareService.calculateFare(pathResult).getFare()).isEqualTo(1250);

        when(pathResult.getTotalDistance()).thenReturn(13);
        assertThat(fareService.calculateFare(pathResult).getFare()).isEqualTo(1350);

        when(pathResult.getTotalDistance()).thenReturn(20);
        assertThat(fareService.calculateFare(pathResult).getFare()).isEqualTo(1450);

        when(pathResult.getTotalDistance()).thenReturn(50);
        assertThat(fareService.calculateFare(pathResult).getFare()).isEqualTo(1250+700+100);

    }
}
