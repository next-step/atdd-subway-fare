package nextstep.subway.domain.path;

public interface PathFinder {
	Path getShortestPath(Long source, Long target);
}
