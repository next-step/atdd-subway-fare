package nextstep.subway.domain;

public class EdgeWeightDistance implements EdgeWeight {

    @Override
    public int getEdgeWeight(Section section) {
        return section.getDistance();
    }

}
