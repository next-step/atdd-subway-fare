package nextstep.subway.unit.mockobj;

import nextstep.subway.path.domain.Path;

public class MockPath extends Path {
    private final int totalDistance;

    public MockPath(int totalDistance) {
        super(new MockLines(), new MockSections());
        this.totalDistance = totalDistance;
    }

    @Override
    public int getTotalDistance() {
        return totalDistance;
    }
}
