package nextstep.subway.applicaion;

import java.util.List;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.member.domain.User;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final MemberService memberService;

    public PathService(
        LineService lineService,
        StationService stationService,
        MemberService memberService
    ) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public PathResponse findPath(PathRequest request, User user) {
        Station upStation = stationService.findById(request.getSource());
        Station downStation = stationService.findById(request.getTarget());
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);
        Path path = subwayMap.findPath(upStation, downStation, request.getType());
        final int fare = path.calculateFare();

        if (user.isGuest()) {
            return PathResponse.of(path, fare);
        }
        final Member member = memberService.findById(user.getId());
        final int discountFare = member.calculateDiscountFare(fare);

        return PathResponse.of(path, discountFare);
    }
}
