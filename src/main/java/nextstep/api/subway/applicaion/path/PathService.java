package nextstep.api.subway.applicaion.path;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.api.SubwayException;
import nextstep.api.auth.aop.principal.UserPrincipal;
import nextstep.api.member.domain.MemberRepository;
import nextstep.api.subway.applicaion.path.dto.PathResponse;
import nextstep.api.subway.applicaion.station.dto.StationResponse;
import nextstep.api.subway.domain.line.LineRepository;
import nextstep.api.subway.domain.path.FareDiscounts;
import nextstep.api.subway.domain.path.PathSelection;
import nextstep.api.subway.domain.station.Station;
import nextstep.api.subway.domain.station.StationRepository;
import nextstep.api.subway.support.SubwayShortestPath;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public PathResponse findShortestPath(final UserPrincipal userPrincipal, final Long sourceId, final Long targetId,
                                         final String type) {
        final var member = memberRepository.getByEmail(userPrincipal.getUsername());

        final var pathType = PathSelection.from(type);
        final var shortestPath = shortestPathOf(sourceId, targetId, pathType);

        return new PathResponse(
                StationResponse.toResponses(shortestPath.getStation()),
                shortestPath.getTotalDistance(),
                shortestPath.getTotalDuration(),
                FareDiscounts.discountFareByAge(shortestPath.getTotalFare(), member.getAge())
       );
    }

    public PathResponse findShortestPath(final Long sourceId, final Long targetId, final String type) {
        final var pathType = PathSelection.from(type);
        final var shortestPath = shortestPathOf(sourceId, targetId, pathType);

        return new PathResponse(
                StationResponse.toResponses(shortestPath.getStation()),
                shortestPath.getTotalDistance(),
                shortestPath.getTotalDuration(),
                shortestPath.getTotalFare()
        );
    }

    public void validateConnected(final Station source, final Station target) {
        shortestPathOf(source.getId(), target.getId(), PathSelection.DISTANCE);
    }

    private SubwayShortestPath shortestPathOf(final Long sourceId, final Long targetId, final PathSelection pathType) {
        if (Objects.equals(sourceId, targetId)) {
            throw new SubwayException(String.format("출발역과 도착역이 동일합니다: 출발역id=%d, 도착역id=%d", sourceId, targetId));
        }

        final var source = stationRepository.getById(sourceId);
        final var target = stationRepository.getById(targetId);

        return shortestPathOf(source, target, pathType);
    }

    private SubwayShortestPath shortestPathOf(final Station source, final Station target,
                                              final PathSelection pathType) {
        final var stations = stationRepository.findAll();
        final var lines = lineRepository.findAll();

        return SubwayShortestPath.builder(stations, lines)
                .source(source)
                .target(target)
                .buildOf(pathType);
    }
}
