package nextstep.subway.path.domain;

import nextstep.subway.path.dto.FarePolicyRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class DistancePolicyTest {

    @DisplayName("10km 이내 모두 1250원")
    @ParameterizedTest
    @ValueSource(ints = {5, 10})
    void calculateBy10km(int distance) {
        // when
        int fare = createDistancePolicy(distance).calculate(createFarePolicyRequest(distance));

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("10km ~ 50km까지 5km당 100원 추가")
    @ParameterizedTest
    @CsvSource({"11,1350", "15,1350", "31,1750", "35,1750", "46,2050", "50,2050"})
    void calculateBy50km(int distance, int expectedFare) {
        // when
        int fare = createDistancePolicy(distance).calculate(createFarePolicyRequest(distance));

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    @DisplayName("10km ~ 50km까지 5km당 100원 추가")
    @ParameterizedTest
    @CsvSource({"51,2150", "58,2150", "59,2250", "66,2250", "67,2350", "74,2350"})
    void calculateOver50km(int distance, int expectedFare) {
        // when
        int fare = createDistancePolicy(distance).calculate(createFarePolicyRequest(distance));

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    private DistancePolicy createDistancePolicy(int distance) {
        return DistancePolicy.choicePolicyByDistance(distance);
    }

    private FarePolicyRequest createFarePolicyRequest(int distance) {
        return FarePolicyRequest
                .builder()
                .distance(distance)
                .build();
    }


}
