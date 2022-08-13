package nextstep.subway.domain.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ElevenToFiftyExtraFareTest {

    private FarePolicy elevenToFiftyExtraFare;

    @BeforeEach
    void setUp() {
        elevenToFiftyExtraFare = new ElevenToFiftyExtraFare();
    }

    @DisplayName("ElevenToFiftyExtraFare 는 10km 초과 시 적용한다.")
    @ParameterizedTest(name = "#{index} - 거리={0}km")
    @CsvSource(value = {"10:false", "11:true", "50:true"}, delimiter = ':')
    void support(int distance, boolean expected) {
        assertThat(elevenToFiftyExtraFare.supports(distance)).isEqualTo(expected);
    }

    @DisplayName("ElevenToFiftyExtraFare 요금정책은 5km 마다 100원씩 추가요금이 적용된다.")
    @ParameterizedTest(name = "#{index} - 거리={0}km, 추가요금={1}")
    @CsvSource(value = {"11:100", "16:200", "21:300", "26:400", "31:500", "36:600", "41:700", "46:800", "51:800"}, delimiter = ':')
    void fare(int distance, int expected) {
        assertAll(() -> {
            assertThat(elevenToFiftyExtraFare.supports(distance)).isTrue();
            assertThat(elevenToFiftyExtraFare.fare(distance)).isEqualTo(expected);
        });
    }
}