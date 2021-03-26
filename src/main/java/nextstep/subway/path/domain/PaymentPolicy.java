package nextstep.subway.path.domain;

import java.util.Arrays;

public interface PaymentPolicy {
    long DEFAULT_COST = 1250;

    Cost cost(PathResult pathResult);

    static long sum(long... values) {
        long sum = Arrays.stream(values).sum();
        return DEFAULT_COST + sum;
    }

    static long calculateOverFare(int distance, double delimiter) {
        return (long) (Math.ceil(distance / delimiter) * 100);
    }
}
