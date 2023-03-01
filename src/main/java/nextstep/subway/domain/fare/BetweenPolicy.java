package nextstep.subway.domain.fare;


import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@RequiredArgsConstructor(access = PROTECTED)
public class BetweenPolicy implements DistancePolicy {

    private final int start;
    private final int end;
    private final int rate;

    @Override
    public int getFare(final int distance) {
        return getFare(distance, start, end, rate);
    }
}
