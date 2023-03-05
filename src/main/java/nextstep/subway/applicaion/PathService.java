package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.member.domain.IdentificationMember;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public PathResponse findPath(IdentificationMember identificationMember, PathRequest pathRequest) {
        Station upStation = stationService.findById(pathRequest.getSource());
        Station downStation = stationService.findById(pathRequest.getTarget());
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathRequest.getType());

        return identificationMember.isAnonymousMember() ?
                getAnonymousPathResponse(path) :
                getLoginMemberPathResponse(identificationMember, path);
    }

    private PathResponse getAnonymousPathResponse(Path path) {
        return PathResponse.of(
                path,
                path.calculateFare()
        );
    }

    private PathResponse getLoginMemberPathResponse(IdentificationMember identificationMember, Path path) {
        Member member = memberService.findMember(identificationMember.getId());
        return PathResponse.of(path, path.calculateFare(member));
    }
}
