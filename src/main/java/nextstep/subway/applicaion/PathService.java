package nextstep.subway.applicaion;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Fare.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;

@Service
public class PathService {
	private final LineService lineService;
	private final StationService stationService;
	private final FareService fareService;

	public PathService(LineService lineService, StationService stationService,
		FareService fareService) {
		this.lineService = lineService;
		this.stationService = stationService;
		this.fareService = fareService;
	}

	public PathResponse findPath(LoginMember user, Long source, Long target, PathType pathType) {
		final Station upStation = stationService.findById(source);
		final Station downStation = stationService.findById(target);
		final List<Line> lines = lineService.findLines();
		final SubwayMap subwayMap = new SubwayMap(lines);
		final Path path = subwayMap.findPath(upStation, downStation, pathType);

		final Fare fare = fareService.calculateTotalFare(user, path.getSections());

		return PathResponse.of(path, fare);
	}
}
