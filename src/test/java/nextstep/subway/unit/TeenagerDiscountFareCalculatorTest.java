package nextstep.subway.unit;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.fare.TeenagerDiscountFareCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import support.auth.context.Authentication;
import support.auth.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TeenagerDiscountFareCalculatorTest {

    private MemberService memberService;
    private Authentication member;
    private Path path;
    private TeenagerDiscountFareCalculator teenagerDiscountFareCalculator;

    @BeforeEach
    void setUp() {
        memberService = mock(MemberService.class);
        member = new Authentication("MEMBER", List.of("ROLE_MEMBER"));
        path = new Path(new Sections());

        teenagerDiscountFareCalculator = new TeenagerDiscountFareCalculator(memberService);
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void teenagerDiscount(int age) {
        유저_준비(age);
        var initialFare = 1000;

        var fare = teenagerDiscountFareCalculator.calculate(path, initialFare);

        assertThat(fare).isEqualTo((int) ((initialFare - 350) * 0.8));
    }

    @ParameterizedTest
    @ValueSource(ints = {12, 19})
    void teenagerDiscountNotApplied(int age) {
        유저_준비(age);
        var initialFare = 1000;

        var fare = teenagerDiscountFareCalculator.calculate(path, initialFare);

        assertThat(fare).isEqualTo(initialFare);
    }

    private void 유저_준비(int age) {
        SecurityContextHolder.getContext().setAuthentication(member);
        when(memberService.findMember((String) member.getPrincipal()))
                .thenReturn(new MemberResponse(1L, (String) member.getPrincipal(), age));
    }
}
