package nextstep.subway.application.service;

import nextstep.member.application.service.MemberService;
import nextstep.member.domain.entity.Member;
import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.application.dto.StationResponse;
import nextstep.subway.domain.*;
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
	private final MemberService memberService;

	public PathService(StationService stationService, SectionService sectionService, MemberService memberService) {
		this.stationService = stationService;
		this.sectionService = sectionService;
		this.memberService = memberService;
	}

	public PathResponse getPath(Long source, Long target, PathType type) {
		PathFinder subwayMap = SubwayMapFactory.getSubwayMap(sectionService.findAll(), type);
		Path path = subwayMap.getShortestPath(source, target);
		int fare = new FareCalculator(path).getFare();

		return createPathResponse(path.getStations(), type, path.getDistance(), path.getDuration(), fare);
	}

	public PathResponse getPath(Long memberId, Long source, Long target, PathType type) {
		Member member = memberService.findMemberById(memberId);

		PathFinder subwayMap = SubwayMapFactory.getSubwayMap(sectionService.findAll(), type);
		Path path = subwayMap.getShortestPath(source, target);
		int fare = new FareCalculator(path, member).getFare();

		return createPathResponse(path.getStations(), type, path.getDistance(), path.getDuration(), fare);
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
