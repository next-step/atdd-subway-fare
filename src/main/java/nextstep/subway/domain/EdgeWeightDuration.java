package nextstep.subway.domain;

public class EdgeWeightDuration implements EdgeWeight {
    @Override
    public int getEdgeWeight(Section section) {
        return section.getDuration();
    }
}
