package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.*;
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
    public PathResponse findPath(LoginMember loginMember, PathRequest pathRequest) {
        Station upStation = stationService.findById(pathRequest.getSource());
        Station downStation = stationService.findById(pathRequest.getTarget());
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, pathRequest.getType());

        return loginMember.isAnonymousMember() ?
                getAnonymousPathResponse(path) :
                getLoginMemberPathResponse(loginMember, path);
    }

    private static PathResponse getAnonymousPathResponse(Path path) {
        return PathResponse.of(
                path,
                Fare.of(
                        new DistanceFarePolicy(path.getMaxExtraFare()),
                        path.getTotalDistance()
                )
        );
    }

    private PathResponse getLoginMemberPathResponse(LoginMember loginMember, Path path) {
        MemberResponse memberResponse = memberService.findMember(loginMember.getId());

        FarePolicy ageDiscountFarePolicy = AgeDiscountFarePolicyFactory.getPolicy(memberResponse.getAge());

        Fare fare = Fare.of(
                new DistanceFarePolicy(
                        ageDiscountFarePolicy,
                        path.getMaxExtraFare()
                ),
                path.getTotalDistance()
        );

        return PathResponse.of(path, fare);
    }
}
