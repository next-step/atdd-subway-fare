package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final FareCalculateDomainService fareCalculateDomainService;
    private final MemberService memberService;

    public PathService(LineService lineService, StationService stationService, FareCalculateDomainService fareCalculateDomainService, MemberService memberService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.fareCalculateDomainService = fareCalculateDomainService;
        this.memberService = memberService;
    }

    public PathResponse findPath(Long source, Long target, PathType pathType, @Nullable LoginMember loginMember) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        Path path = getPath(pathType, upStation, downStation, subwayMap);
        int fare = getFare(path, loginMember);
        return PathResponse.of(path, fare);
    }

    private Path getPath(PathType pathType, Station upStation, Station downStation, SubwayMap subwayMap) {
        if (pathType == PathType.DURATION) {
            return subwayMap.findPath(upStation, downStation, Section::getDuration);
        }
        return subwayMap.findPath(upStation, downStation, Section::getDistance);
    }

    private int getFare(Path path, @Nullable LoginMember loginMember) {
        if (loginMember != null) {
            MemberResponse member = memberService.findMember(loginMember.getId());
            return fareCalculateDomainService.calculateFareAmount(path, member.getAge());
        }
        return fareCalculateDomainService.calculateFareAmount(path);
    }
}
