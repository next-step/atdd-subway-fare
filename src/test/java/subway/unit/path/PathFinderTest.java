package subway.unit.path;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PathFinderTest { // TODO: 제거해야된다 까먹지마라

    private static final long BASIC_FARE = 1250L;

    @Test
    void calculate() {
        assertThat(calculateFare(5)).isEqualTo(1250L);
        assertThat(calculateFare(10)).isEqualTo(1250L);
        assertThat(calculateFare(11)).isEqualTo(1350L);
        assertThat(calculateFare(15)).isEqualTo(1350L);
        assertThat(calculateFare(16)).isEqualTo(1450L);
        assertThat(calculateFare(20)).isEqualTo(1450L);
        assertThat(calculateFare(21)).isEqualTo(1550L);
        assertThat(calculateFare(25)).isEqualTo(1550L);
        assertThat(calculateFare(26)).isEqualTo(1650L);
        assertThat(calculateFare(30)).isEqualTo(1650L);
        assertThat(calculateFare(31)).isEqualTo(1750L);
        assertThat(calculateFare(35)).isEqualTo(1750L);
        assertThat(calculateFare(36)).isEqualTo(1850L);
        assertThat(calculateFare(40)).isEqualTo(1850L);
        assertThat(calculateFare(41)).isEqualTo(1950L);
        assertThat(calculateFare(45)).isEqualTo(1950L);
        assertThat(calculateFare(46)).isEqualTo(2050L);
        assertThat(calculateFare(50)).isEqualTo(2050L);
        assertThat(calculateFare(51)).isEqualTo(2150L);
        assertThat(calculateFare(58)).isEqualTo(2150L);
        assertThat(calculateFare(59)).isEqualTo(2250L);
    }

    protected long calculateFare(final long distance) {
        long totalFare = BASIC_FARE;
        if (distance > 10 && distance <= 50) {
            totalFare += calculateOverFareWithTenDistance(distance - 10);
        }
        if (distance > 50) {
            totalFare += calculateOverFareWithTenDistance(40);
            totalFare += calculateOverFareWithFiftyDistance(distance - 50);
        }
        return totalFare;
    }
    private long calculateOverFareWithTenDistance(long distance) {
        return (long) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private long calculateOverFareWithFiftyDistance(long distance) {
        return (long) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

}
