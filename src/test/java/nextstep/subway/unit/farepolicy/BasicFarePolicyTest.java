package nextstep.subway.unit.farepolicy;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.domain.map.Path;
import nextstep.subway.domain.farepolicy.BasicFarePolicy;

@DisplayName("기본 요금 정책 테스트")
@ExtendWith(MockitoExtension.class)
public class BasicFarePolicyTest {
    @Mock
    private Path path;

    @DisplayName("타지 않으면 요금을 부과하지 않는다.")
    @Test
    void calculateDontTake() {
        when(path.extractDistance()).thenReturn(0);

        assertThat(new BasicFarePolicy().calculate(path)).isZero();
    }

    @ValueSource(ints = { 1, 10 })
    @DisplayName("1km 이상 타면 무조건 1250원의 요금이 부과 된다.")
    @ParameterizedTest
    void calculate(int distance) {
        when(path.extractDistance()).thenReturn(distance);

        assertThat(new BasicFarePolicy().calculate(path)).isEqualTo(1250);
    }
}
