package nextstep.path.service;

import nextstep.line.entity.Line;
import nextstep.line.service.LineService;
import nextstep.member.application.MemberService;
import nextstep.member.domain.AnonymousLoginMember;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.path.dto.Path;
import nextstep.path.dto.PathResponse;
import nextstep.station.service.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathFinder {

    private StationService stationService;
    private LineService lineService;
    private PathService pathService;
    private MemberService memberService;
    private CalculateFareService calculateFareService;

    public PathFinder(StationService stationService, LineService lineService, PathService pathService, MemberService memberService, CalculateFareService calculateFareService) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathService = pathService;
        this.memberService = memberService;
        this.calculateFareService = calculateFareService;
    }

    @Transactional(readOnly = true)
    public PathResponse retrieveStationPath(final LoginMember loginMember, final String type, final Long source, final Long target) {
        validateStationExist(source, target);
        List<Line> lineList = lineService.getAllLines();
        Member member = findMemberByOptionalLoginMember(loginMember);
        Path path = pathService.findPath(member, type, source, target, lineList);
        calculateFareService.calculateFare(path);
        return path.createPathResponse();
    }

    private void validateStationExist(final Long source, final Long target) {
        stationService.getStationByIdOrThrow(source);
        stationService.getStationByIdOrThrow(target);
    }

    private Member findMemberByOptionalLoginMember(LoginMember loginMember) {
        if (loginMember instanceof AnonymousLoginMember) return null;
        return memberService.findMemberByEmail(loginMember.getEmail());
    }

}

