package nextstep.subway.applicaion;

import org.springframework.stereotype.Service;

import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.ui.AuthenticationException;
import nextstep.subway.domain.Fare.Fare;
import nextstep.subway.domain.Fare.handler.AgeFarePolicyHandler;
import nextstep.subway.domain.Fare.handler.DistanceFarePolicyHandler;
import nextstep.subway.domain.Fare.handler.FareHandler;
import nextstep.subway.domain.Fare.handler.LineFarePolicyHandler;
import nextstep.subway.domain.Sections;

@Service
public class FareService {
	private final MemberService memberService;
	private final FareHandler baseFareHandler;

	public FareService(MemberService memberService) {
		this.memberService = memberService;
		DistanceFarePolicyHandler distanceFarePolicyHandler = new DistanceFarePolicyHandler();
		LineFarePolicyHandler lineFarePolicyHandler = new LineFarePolicyHandler();
		AgeFarePolicyHandler ageFarePolicyHandler = new AgeFarePolicyHandler();

		baseFareHandler = distanceFarePolicyHandler;
		baseFareHandler.setHandler(lineFarePolicyHandler);
		lineFarePolicyHandler.setHandler(ageFarePolicyHandler);
	}

	public Fare calculateTotalFare(LoginMember loginMember, Sections sections) {
		Fare fare = getFare(loginMember, sections);

		baseFareHandler.calculate(fare);

		return fare;
	}

	public Fare getFare(LoginMember loginMember, Sections sections) {
		Fare fare;
		Long memberId;
		try {
			memberId = loginMember.getId();
			fare = Fare.of(memberService.getMember(memberId), sections);
		} catch (AuthenticationException exception) {
			fare = Fare.of(null, sections);
		}
		return fare;
	}
}
