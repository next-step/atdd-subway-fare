package nextstep.subway.unit;

import nextstep.subway.domain.policy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FarePoliciesTest {

    private FarePolicies farePolicies;


    @BeforeEach
    void setup() {
        //given 요금 정책을 확정한다.
        FarePolicy base = new FixedFarePolicy(1250);
        FarePolicy ten = new BetweenUnitFarePolicy(10, 50, 5, 100);
        FarePolicy fifth = new GreaterUnitFarePolicy(50, 8, 100);
        farePolicies = new FarePolicies();
        farePolicies.addPolicies(base, ten, fifth);
    }

    @Test
    @DisplayName("8km")
    void km_8() {
        //then 8km면 요금은 1250원이다.
        assertThat(farePolicies.calculate(8)).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km")
    void km_10() {
        //then 10km면 요금은 1250원이다.
        assertThat(farePolicies.calculate(10)).isEqualTo(1250);
    }
    @Test
    @DisplayName("12km")
    void km_12() {
        //then 12km면 요금은 1350원이다.
        assertThat(farePolicies.calculate(12)).isEqualTo(1350);
    }
    @Test
    @DisplayName("16km")
    void km_16() {
        //then 8km면 요금은 1450원이다.
        assertThat(farePolicies.calculate(16)).isEqualTo(1450);
    }
    @Test
    @DisplayName("50km")
    void km_50() {
        //then 8km면 요금은 2050원이다.
        assertThat(farePolicies.calculate(50)).isEqualTo(2050);
    }

    @Test
    @DisplayName("60km")
    void km_60() {
        //then 8km면 요금은 2250원이다.
        assertThat(farePolicies.calculate(60)).isEqualTo(2250);
    }
    @Test
    @DisplayName("65km")
    void km_65() {
        //then 8km면 요금은 2250원이다.
        assertThat(farePolicies.calculate(65)).isEqualTo(2250);
    }



}
