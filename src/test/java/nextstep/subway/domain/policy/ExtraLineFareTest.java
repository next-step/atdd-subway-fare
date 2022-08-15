package nextstep.subway.domain.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ExtraLineFareTest {

    private FarePolicy extraLineFare;

    @BeforeEach
    void setUp() {
        extraLineFare = new ExtraLineFare();
    }

    @DisplayName("노선별 추가 요금 정책은 항상 적용한다.")
    @ParameterizedTest(name = "#{index} - 거리={0}km")
    @ValueSource(ints = {1, 10, 11, 50, 51})
    void support(int distance) {
        PathByFare pathByFare = PathByFare.builder().distance(distance).build();

        assertThat(extraLineFare.supports(pathByFare)).isTrue();
    }
}