package nextstep.subway.domain.fare;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BetweenFarePolicy implements FarePolicy {

    private final int start;
    private final int end;
    private final int rate;

    @Override
    public int getFare(final int distance) {
        return getFare(distance, start, end, rate);
    }
}
