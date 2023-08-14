package nextstep.subway.applicaion;

import nextstep.auth.AuthenticationException;
import nextstep.auth.principal.LoginUserPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberRepository memberRepository;

    public PathService(LineService lineService, StationService stationService, MemberRepository memberRepository) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public PathResponse findPath(Long source, Long target, SubwayMapType type, LoginUserPrincipal userPrincipal) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = type.getSubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation);

        if (userPrincipal instanceof UserPrincipal) {
            Member member = memberRepository.findByEmail(userPrincipal.getUsername()).orElseThrow(AuthenticationException::new);
            return PathResponse.of(path, member);
        }
        return PathResponse.of(path, null);
    }
}
