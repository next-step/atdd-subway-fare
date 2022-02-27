package nextstep.subway.domain;

@FunctionalInterface
public interface Edge {
	int getWeight(Section section);
}
