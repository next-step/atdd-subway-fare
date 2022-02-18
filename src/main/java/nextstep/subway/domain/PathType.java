package nextstep.subway.domain;

public enum PathType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration),
    ;

    private PathCostFunction pathCostFunction;

    PathType(PathCostFunction pathCostFunction) {
        this.pathCostFunction = pathCostFunction;
    }

    public int cost(Section section) {
        return pathCostFunction.cost(section);
    }
}
