package nextstep.subway.path;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.SearchType;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PathService {

	private final LineService lineService;
	private final StationService stationService;

	public PathResponse findPathGuest(PathRequest pathRequest) {
		Path path = findPath(pathRequest.getSource(), pathRequest.getTarget(), pathRequest.getType());
		return PathResponse.of(path);
	}

	public PathResponse findPathForMember(LoginMember loginMember, PathRequest pathRequest) {
		Path path = findPath(pathRequest.getSource(), pathRequest.getTarget(), pathRequest.getType());
		return PathResponse.of(path);
	}

	private Path findPath(Long source, Long target, SearchType type) {
		Station upStation = stationService.findById(source);
		Station downStation = stationService.findById(target);
		List<Line> lines = lineService.findLines();
		SubwayMap subwayMap = new SubwayMap(lines);
		return subwayMap.findPath(upStation, downStation, type);
	}
}
