package nextstep.subway.domain.fare;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
public class FirstPolicy implements DistancePolicy {
    @Override
    public int getFare(final int distance) {
        return DEFAULT_FARE;
    }
}
