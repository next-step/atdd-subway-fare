package nextstep.subway.unit.farepolicy;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.domain.map.Path;
import nextstep.subway.domain.farepolicy.UpTo50FarePolicy;
import nextstep.subway.domain.farepolicy.base.FarePolicy;

@DisplayName("50km 까지의 요금 정책 테스트")
@ExtendWith(MockitoExtension.class)
public class UpTo50FarePolicyTest {
    @Mock
    private Path path;
    private FarePolicy farePolicy;

    @BeforeEach
    void setUp() {
        farePolicy = new UpTo50FarePolicy();
    }

    @CsvSource({
        "10,0",
        "11,100",
        "50,900",
        "55,900",
        "100,900"
    })
    @DisplayName("50km 까지는 10km를 초과하는 거리에 대해 5km당 100원의 요금이 부과 된다.")
    @ParameterizedTest
    void calculateUpTo50(int distance, int cost) {
        when(path.extractDistance()).thenReturn(distance);

        assertThat(farePolicy.calculate(path)).isEqualTo(cost);
    }
}
