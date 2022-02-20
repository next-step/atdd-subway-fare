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
import nextstep.subway.domain.farepolicy.MoreThan50FarePolicy;
import nextstep.subway.domain.farepolicy.base.FarePolicy;

@DisplayName("50km 초과의 요금 정책 테스트")
@ExtendWith(MockitoExtension.class)
public class MoreThan50FarePolicyTest {
    @Mock
    private Path path;
    private FarePolicy farePolicy;

    @BeforeEach
    void setUp() {
        farePolicy = new MoreThan50FarePolicy();
    }

    @CsvSource({
        "50,0",
        "51,100",
        "59,200",
        "67,300"
    })
    @DisplayName("50km를 초과하면 8km당 100원이 부과된다.")
    @ParameterizedTest
    void calculateMoreThan50(int distance, int cost) {
        when(path.extractDistance()).thenReturn(distance);

        assertThat(farePolicy.calculate(path)).isEqualTo(cost);
    }
}
