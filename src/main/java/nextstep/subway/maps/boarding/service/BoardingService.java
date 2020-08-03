package nextstep.subway.maps.boarding.service;

import nextstep.subway.maps.boarding.domain.Boarding;
import nextstep.subway.maps.boarding.domain.DistanceProportionFareCalculationPolicy;
import nextstep.subway.maps.boarding.domain.ExtraFareCalculationPolicy;
import nextstep.subway.maps.boarding.domain.FareCalculationPolicyGroup;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.members.member.application.MemberService;
import nextstep.subway.members.member.dto.MemberResponse;
import org.springframework.stereotype.Service;

@Service
public class BoardingService {

    private final FareCalculationPolicyGroup policyGroup;
    private final MemberService memberService;

    public BoardingService(
            MemberService memberService,
            DistanceProportionFareCalculationPolicy distanceProportionFareCalculationPolicy,
            ExtraFareCalculationPolicy extraFareCalculationPolicy) {
        policyGroup = new FareCalculationPolicyGroup(
                distanceProportionFareCalculationPolicy,
                extraFareCalculationPolicy
        );
        this.memberService = memberService;
    }

    public int calculateFare(SubwayPath subwayPath, Long memberId) {
        final MemberResponse passenger = getMemberResponse(memberId);
        final Boarding boarding = new Boarding(passenger, subwayPath);
        return policyGroup.calculateFare(boarding);
    }

    private MemberResponse getMemberResponse(Long memberId) {
        MemberResponse passenger;
        try {
            passenger = memberService.findMember(memberId);
        } catch (RuntimeException e) {
            passenger = null;
        }
        return passenger;
    }
}
