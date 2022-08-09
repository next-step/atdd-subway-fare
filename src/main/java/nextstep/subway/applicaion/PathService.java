package nextstep.subway.applicaion;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.path.PathBaseCode;
import nextstep.subway.domain.path.PathFinder;

@Service
public class PathService {
	private LineService lineService;
	private StationService stationService;

	public PathService(LineService lineService, StationService stationService) {
		this.lineService = lineService;
		this.stationService = stationService;
	}

	public PathResponse findPath(Long source, Long target, PathBaseCode pathBaseCode) {
		Station upStation = stationService.findById(source);
		Station downStation = stationService.findById(target);
		List<Line> lines = lineService.findLines();
		PathFinder pathFinder = pathBaseCode.getPathFinderClass(lines);
		Path path = pathFinder.findPath(upStation, downStation);

		return PathResponse.of(path);
	}

}
