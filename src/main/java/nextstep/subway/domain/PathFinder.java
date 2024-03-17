package nextstep.subway.domain;

public interface PathFinder {
	Path getShortestPath(Long source, Long target);
}
