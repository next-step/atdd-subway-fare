package nextstep.subway.domain.farepolicy;

public class DistanceFareRange {
    private final int minRange;
    private final int maxRange;

    public DistanceFareRange(int minRange, int maxRange) {
        this.minRange = minRange;
        this.maxRange = maxRange;
    }

    public int getMinRange() {
        return minRange;
    }

    public int getMaxRange() {
        return maxRange;
    }
}
