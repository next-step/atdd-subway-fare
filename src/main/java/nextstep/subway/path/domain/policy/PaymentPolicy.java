package nextstep.subway.path.domain.policy;

import nextstep.subway.path.dto.CostRequest;

import java.util.Arrays;

public interface PaymentPolicy {
    long DEFAULT_COST = 1250;

    CostRequest cost(CostRequest costRequest);

    static long sum(long... values) {
        long sum = Arrays.stream(values).sum();
        return DEFAULT_COST + sum;
    }

    static long calculateOverFare(int distance, double delimiter) {
        return (long) (Math.ceil(distance / delimiter) * 100);
    }
}
