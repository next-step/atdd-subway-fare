package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.FindPathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.domain.fare.Fare;
import nextstep.subway.domain.fare.FareService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
	private LineService lineService;
	private StationService stationService;
	private FareService fareService;

	public PathService(LineService lineService, StationService stationService, FareService fareService) {
		this.lineService = lineService;
		this.stationService = stationService;
		this.fareService = fareService;
	}

	public PathResponse findPath(FindPathRequest request, Integer age) {
		Station upStation = stationService.findById(request.getSource());
		Station downStation = stationService.findById(request.getTarget());
		List<Line> lines = lineService.findLines();

		SubwayMap subwayMap = new SubwayMap(lines);
		Path path = subwayMap.findPath(upStation, downStation, request.getType());
		Fare totalFare = fareService.calculateAmount(path, age);
		return PathResponse.of(path, totalFare);
	}
}
