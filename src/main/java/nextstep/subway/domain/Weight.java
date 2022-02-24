package nextstep.subway.domain;

@FunctionalInterface
public interface Weight {
	int getWeight(Section section);
}
