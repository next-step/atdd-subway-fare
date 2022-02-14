package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.domain.Path;
import nextstep.subway.domain.farepolicy.BasicFarePolicy;
import nextstep.subway.domain.farepolicy.DistanceFarePolicy;
import nextstep.subway.domain.farepolicy.DistanceFareRange;
import nextstep.subway.domain.farepolicy.FarePolicy;

@DisplayName("경로 결과 테스트")
@ExtendWith(MockitoExtension.class)
public class PathTest {
    @Mock
    private Path path;


    @CsvSource({
        "9,1250",
        "12,1350",
        "50,2150",
        "51,2250",
        "59,2350",
    })
    @DisplayName("요금 계산 테스트")
    @ParameterizedTest
    void extractTotalCost(int distance, int totalCost) {
        List<FarePolicy> farePolicies = Arrays.asList(
            new BasicFarePolicy(),
            new DistanceFarePolicy(
                new DistanceFareRange(50, Integer.MAX_VALUE), 8, 100
            ),
            new DistanceFarePolicy(
                new DistanceFareRange(10, 50), 5, 100
            )
        );
        when(path.extractDistance()).thenReturn(distance);

        assertThat(path.extractTotalCost(farePolicies)).isEqualTo(totalCost);
    }
}
