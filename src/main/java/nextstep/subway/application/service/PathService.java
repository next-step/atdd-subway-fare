package nextstep.subway.application.service;

import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.application.dto.StationResponse;
import nextstep.subway.domain.FareCalculator;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.PathFinderFactory;
import nextstep.subway.ui.controller.PathType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {
	private final StationService stationService;
	private final SectionService sectionService;

	public PathService(StationService stationService, SectionService sectionService) {
		this.stationService = stationService;
		this.sectionService = sectionService;
	}

	public PathResponse getPath(Long source, Long target, PathType type) {
		PathFinder pathFinder = PathFinderFactory.getPathFinder(sectionService.findAll(), type);
		Path path = pathFinder.getPath(source, target);
		int fare = new FareCalculator(path).getFare();

		return createPathResponse(path.getVertexs(), type, path.getDistance(), path.getDuration(), fare);
	}

	private PathResponse createPathResponse(List<Long> stations, PathType type, int distance, int duration, int fare) {
		return new PathResponse(
				stations.stream()
						.map(id -> new StationResponse(id, stationService.findStationById(id).getName()))
						.collect(Collectors.toList()),
				type,
				distance,
				duration,
				fare);
	}
}
