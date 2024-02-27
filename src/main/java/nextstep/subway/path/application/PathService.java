package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.application.dto.PathRequest;
import nextstep.subway.path.application.dto.PathResponse;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathType;
import nextstep.subway.station.application.dto.StationResponseFactory;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final PathFinder pathFinder;
    private final LineRepository lineRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public PathService(PathFinder pathFinder,
                       LineRepository lineRepository,
                       MemberRepository memberRepository,
                       StationRepository stationRepository) {
        this.pathFinder = pathFinder;
        this.lineRepository = lineRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortCut(LoginMember loginMember,
                                     PathRequest pathRequest) {
        Lines lines = Lines.from(lineRepository.findAllFetchJoin());
        Path path = pathFinder.shortcut(lines,
                getStation(pathRequest.getSource()),
                getStation(pathRequest.getTarget()),
                PathType.valueOf(pathRequest.getType()));

        return new PathResponse(StationResponseFactory.create(path.getStations()),
                path.getDistance(),
                path.getDuration(),
                getFare(loginMember, path, lines));
    }

    private Long getFare(LoginMember loginMember,
                         Path path,
                         Lines lines) {
        return memberRepository.findByEmail(loginMember.getEmail())
                .map(member -> path.fare(lines, member))
                .orElse(path.fare(lines));
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException("해당 지하철역 정보를 찾지 못했습니다."));
    }
}
