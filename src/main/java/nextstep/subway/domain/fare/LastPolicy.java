package nextstep.subway.domain.fare;

import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@RequiredArgsConstructor(access = PROTECTED)
public class LastPolicy implements DistancePolicy {

    private final int start;
    private final int rate;

    @Override
    public int getFare(final int distance) {
        if (MAX_DISTANCE < distance) {
            throw new InvalidDistanceException();
        }

        return getFare(distance, start, MAX_DISTANCE, rate);
    }
}
