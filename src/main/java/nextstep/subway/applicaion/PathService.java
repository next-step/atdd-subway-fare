package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FindPathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
	private LineService lineService;
	private StationService stationService;

	public PathService(LineService lineService, StationService stationService) {
		this.lineService = lineService;
		this.stationService = stationService;
	}

	public PathResponse findPath(FindPathRequest request, Integer age) {
		Station upStation = stationService.findById(request.getSource());
		Station downStation = stationService.findById(request.getTarget());
		List<Line> lines = lineService.findLines();

		SubwayMap subwayMap = new SubwayMap(lines);
		Path path = subwayMap.findPath(upStation, downStation, request.getType());
		int totalFare = Fare.calculateAmount(path, age);
		return PathResponse.of(path, totalFare);
	}
}
