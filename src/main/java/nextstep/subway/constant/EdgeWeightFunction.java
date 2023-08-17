package nextstep.subway.constant;

@FunctionalInterface
public interface EdgeWeightFunction<EdgeWeight, Integer>  {
    Integer apply(EdgeWeight section);

}
