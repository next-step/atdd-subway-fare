package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.payment.PaymentHandler;
import nextstep.subway.util.discount.Adult;
import nextstep.subway.util.discount.DiscountAgePolicy;
import nextstep.subway.util.discount.AgeFactory;
import org.springframework.stereotype.Service;
import support.auth.userdetails.AnonymousUser;
import support.auth.userdetails.UserDetails;

import java.util.List;

@Service
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private MemberService memberService;
    private AgeFactory ageFactory;

    public PathService(LineService lineService, StationService stationService, MemberService memberService, AgeFactory ageFactory) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
        this.ageFactory = ageFactory;
    }

    public PathResponse findPath(Long source, Long target, UserDetails user) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        Path path = subwayMap.findPath(upStation, downStation);

        Fare fare = calculateFare(path, user);

        return PathResponse.of(path, fare.fare());
    }

    private Fare calculateFare(Path path, UserDetails user) {
        PaymentHandler paymentHandler = new PaymentHandler(path, findLoginMemberAge(user));
        Fare fare = Fare.from(0);
        paymentHandler.calculate(fare);
        return fare;
    }

    private DiscountAgePolicy findLoginMemberAge(UserDetails user) {
        if (user instanceof AnonymousUser) {
            return new Adult();
        }

        String loginEmail = (String) user.getUsername();
        MemberResponse loginMember = memberService.findMember(loginEmail);

        return ageFactory.findUsersAge(loginMember.getAge());
    }
}
