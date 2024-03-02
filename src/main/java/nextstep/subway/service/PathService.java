package nextstep.subway.service;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.controller.dto.PathResponse;
import nextstep.subway.controller.dto.StationResponse;
import nextstep.subway.domain.*;
import nextstep.subway.domain.chain.FareHandlerFactory;
import nextstep.subway.repository.SectionRepository;
import nextstep.subway.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PathService {

    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final PathFinder pathFinder;
    private final FareHandlerFactory fareHandlerFactory;

    public PathResponse findPaths(String email, Long sourceId, Long targetId, String pathType) {
        Station source = stationRepository.getBy(sourceId);
        Station target = stationRepository.getBy(targetId);

        Paths paths = new Paths(pathFinder.findPaths());
        List<Station> stations = paths.findShortestPath(source, target, PathType.of(pathType));

        long distance = paths.findShortestValue(source, target, PathType.DISTANCE);
        long duration = paths.findShortestValue(source, target, PathType.DURATION);
        long fareByDistance = fareHandlerFactory.calculateFare(distance);

        Member member = memberRepository.getBy(email);
        FareAgeGroup fareAgeGroup = FareAgeGroup.of(member.getAge());

        Sections sections = new Sections(sectionRepository.findAll());
        Lines lines = lines(sections, stations);
        long plusExtraFare = lines.calculatePlusExtraFare(fareAgeGroup);

        long discountExtraFare = fareAgeGroup.calculateDiscountFare(fareByDistance);

        long totalFare = fareByDistance + plusExtraFare - discountExtraFare;

        return new PathResponse(StationResponse.listOf(stations), distance, duration, totalFare);
    }

    private Lines lines(Sections sections, List<Station> stations) {
        List<Line> lines = sections.findLinesBy(stations);
        return new Lines(lines);
    }

}
