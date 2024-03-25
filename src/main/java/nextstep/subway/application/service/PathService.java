package nextstep.subway.application.service;

import nextstep.auth.domain.LoginMember;
import nextstep.member.application.service.MemberService;
import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.application.dto.StationResponse;
import nextstep.subway.domain.fare.FareCalculator;
import nextstep.subway.domain.path.Path;
import nextstep.subway.domain.path.PathFinder;
import nextstep.subway.domain.path.SubwayMapFactory;
import nextstep.subway.ui.controller.PathType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {
	private final StationService stationService;
	private final SectionService sectionService;
	private final MemberService memberService;

	public PathService(StationService stationService, SectionService sectionService, MemberService memberService) {
		this.stationService = stationService;
		this.sectionService = sectionService;
		this.memberService = memberService;
	}

	public PathResponse getPath(LoginMember loginMember, Long source, Long target, PathType type) {
		PathFinder subwayMap = SubwayMapFactory.getSubwayMap(sectionService.findAll(), type);
		Path path = subwayMap.getShortestPath(source, target);

		if(loginMember.isLogin()) {
			return createPathResponse(path, type, new FareCalculator(path, memberService.findMemberById(loginMember.getId())).getFare());
		}

		return createPathResponse(path, type, new FareCalculator(path).getFare());
	}

	private PathResponse createPathResponse(Path path, PathType type, int fare) {
		return new PathResponse(
				path.getStations().stream()
						.map(id -> new StationResponse(id, stationService.findStationById(id).getName()))
						.collect(Collectors.toList()),
				type,
				path.getDistance(),
				path.getDuration(),
				fare);
	}
}
