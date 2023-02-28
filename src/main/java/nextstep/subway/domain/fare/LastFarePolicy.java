package nextstep.subway.domain.fare;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LastFarePolicy implements FarePolicy {

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
