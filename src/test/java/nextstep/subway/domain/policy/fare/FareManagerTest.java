package nextstep.subway.domain.policy.fare;

import nextstep.subway.domain.FareManagerLoaderTest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.policy.fare.DefaultFare;
import nextstep.subway.domain.policy.fare.ElevenToFiftyExtraFare;
import nextstep.subway.domain.policy.fare.ExtraLineFare;
import nextstep.subway.domain.policy.fare.FareManager;
import nextstep.subway.domain.policy.fare.OverFiftyExtraFare;
import nextstep.subway.domain.policy.fare.PathByFare;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FareManagerTest extends FareManagerLoaderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private List<Line> lines;
    private List<Line> noExtraFareLines;

    @BeforeEach
    void setUp() {
        FareManager.clearPolicy();
        FareManager.addPolicy(new DefaultFare());
        FareManager.addPolicy(new ElevenToFiftyExtraFare());
        FareManager.addPolicy(new OverFiftyExtraFare());
        FareManager.addPolicy(new ExtraLineFare());

        신분당선 = new Line("신분당선", "red", 900);
        이호선 = new Line("이호선", "green", 300);
        삼호선 = new Line("삼호선", "orange", 0);

        lines = List.of(신분당선, 이호선, 삼호선);
        noExtraFareLines = List.of(삼호선);
    }

    @DisplayName("추가 요금이 없는 노선에서 10km 까지는 기본 요금을 반환한다.")
    @ParameterizedTest(name = "#{index} - 거리={0}km")
    @ValueSource(ints = {1, 10})
    void fare(int distance) {
        PathByFare pathByFare = PathByFare.builder()
                .distance(distance)
                .lines(noExtraFareLines)
                .build();

        assertThat(FareManager.fare(pathByFare)).isEqualTo(1_250);
    }

    @DisplayName("추가 요금이 없는 노선에서 10km 초과 ~ 50km 이하의 경우 기본운임과 5km 마다 100원 추가된다.")
    @ParameterizedTest(name = "#{index} - 거리={0}km, 추가요금={1}")
    @CsvSource(value = {"11:1350", "16:1450", "21:1550", "26:1650", "31:1750", "36:1850", "41:1950", "46:2050", "50:2050"}, delimiter = ':')
    void fare_extra(int distance, int expected) {
        PathByFare pathByFare = PathByFare.builder()
                .distance(distance)
                .lines(noExtraFareLines)
                .build();

        assertThat(FareManager.fare(pathByFare)).isEqualTo(expected);
    }

    @DisplayName("추가 요금이 없는 노선에서 50km 초과시 기본운임과 50km 이하의 초과운임과 8km 마다 100원이 추가된다.")
    @ParameterizedTest(name = "#{index} - 거리={0}km, 추가요금={1}")
    @CsvSource(value = {"51:2150", "59:2250", "67:2350"}, delimiter = ':')
    void fare_extra_over_fifty(int distance, int expected) {
        PathByFare pathByFare = PathByFare.builder()
                .distance(distance)
                .lines(noExtraFareLines)
                .build();

        assertThat(FareManager.fare(pathByFare)).isEqualTo(expected);
    }

    @DisplayName("추가 요금이 있는 노선에서 10km 까지는 기본 요금과 노선의 추가요금이 추가된다.")
    @ParameterizedTest(name = "#{index} - 거리={0}km")
    @ValueSource(ints = {1, 10})
    void fare_extra_lines(int distance) {
        PathByFare pathByFare = PathByFare.builder()
                .distance(distance)
                .lines(lines)
                .build();

        assertThat(FareManager.fare(pathByFare)).isEqualTo(2_150);
    }
}