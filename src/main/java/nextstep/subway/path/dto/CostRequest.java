package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Cost;
import nextstep.subway.path.domain.PathResult;

public class CostRequest {
    private PathResult pathResult;
    private Cost cost;
    private int age;

    public CostRequest(PathResult pathResult, Cost cost, int age) {
        this.pathResult = pathResult;
        this.cost = cost;
        this.age = age;
    }

    public static CostRequest of(PathResult pathResult, Integer age) {
        return new CostRequest(pathResult, Cost.from(0), age);
    }

    public static CostRequest of(PathResult pathResult) {
        return new CostRequest(pathResult, Cost.from(0), 0);
    }

    public PathResult getPathResult() {
        return pathResult;
    }

    public Cost getCost() {
        return cost;
    }

    public int getAge() {
        return age;
    }

    public void updateCost(Cost cost) {
        this.cost = cost;
    }

    public void addCost(long cost) {
        updateCost(new Cost(this.cost.getCost() + cost));
    }

    public int getDistance() {
        return pathResult.getTotalDistance();
    }
}
