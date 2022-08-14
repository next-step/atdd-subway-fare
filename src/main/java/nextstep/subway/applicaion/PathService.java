package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayMap;
import nextstep.subway.payment.PaymentHandler;
import nextstep.subway.payment.PaymentRequest;
import nextstep.subway.payment.PaymentRequestImpl;
import org.springframework.stereotype.Service;
import support.auth.userdetails.AnonymousUser;
import support.auth.userdetails.UserDetails;

import java.util.List;

@Service
public class PathService {
    private static final int ANONYMOUS_AGE = 0;

    private LineService lineService;
    private StationService stationService;
    private MemberService memberService;
    private PaymentHandler paymentHandler;

    public PathService(LineService lineService, StationService stationService, MemberService memberService, PaymentHandler paymentHandler) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
        this.paymentHandler = paymentHandler;
    }

    public PathResponse findPath(Long source, Long target, UserDetails user) {
        Station upStation = stationService.findById(source);
        Station downStation = stationService.findById(target);
        List<Line> lines = lineService.findLines();
        SubwayMap subwayMap = new SubwayMap(lines);

        Path path = subwayMap.findPath(upStation, downStation);
        PaymentRequest paymentRequest = paymentRequest(path, user);

        return PathResponse.of(path, paymentRequest.getFare().fare());
    }

    private PaymentRequest paymentRequest(Path path, UserDetails user) {
        int loginMemberAge = getLoginUserAge(user);

        PaymentRequestImpl paymentRequest = PaymentRequestImpl.of(path, loginMemberAge);

        paymentHandler.calculate(paymentRequest);

        return paymentRequest;
    }

    private int getLoginUserAge(UserDetails user) {
        if (user instanceof AnonymousUser) {
            return ANONYMOUS_AGE;
        }

        String loginEmail = (String) user.getUsername();
        MemberResponse loginMember = memberService.findMember(loginEmail);

        return loginMember.getAge();
    }
}
