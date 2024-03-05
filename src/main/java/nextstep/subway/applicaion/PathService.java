package nextstep.subway.applicaion;

import java.util.List;
import java.util.Optional;
import nextstep.auth.domain.LoginMember;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.PathFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final PathFinder pathFinder;
    private final MemberService memberService;

    public PathService(LineRepository lineRepository, PathFinder pathFinder, MemberService memberService) {
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
        this.memberService = memberService;
    }

    public PathResponse getPath(PathRequest request, LoginMember loginMember) {
        final List<Line> lines = lineRepository.findAll();

        Member member = null;
        if (loginMember != null) {
           member = memberService.findMemberByEmail(loginMember.getEmail());
        }

        return pathFinder.findPath(request, lines, Optional.ofNullable(member));
    }
}
