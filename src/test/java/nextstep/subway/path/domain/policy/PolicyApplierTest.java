package nextstep.subway.path.domain.policy;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("요금정책 테스트 - 실제 적용중인 정책")
@ExtendWith(MockitoExtension.class)
class PolicyApplierTest {

    @Mock
    PathResult pathResult;
    @Mock
    LoginMember loginMember;

    @ParameterizedTest(name = "적용중인 요금정책 테스트")
    @CsvSource({
            "11, 700, 20, 2050"
            ,"11, 900, 20, 2250"
            ,"11, 700, 10, 850"
            ,"11, 700, 15, 1360"
    })
    void 적용중인_요금정책_테스트(int distance, int lineFare, int age, int expectedFare){
        // given
        when(pathResult.getTotalDistance()).thenReturn(distance);
        when(pathResult.getMaxExtraFare()).thenReturn(lineFare);
        when(loginMember.getAge()).thenReturn(age);

        // when
        PolicyApplier policyApplier = new PolicyApplier();
        Fare fare = policyApplier.applyFarePolicy(pathResult, loginMember);

        // then
        assertThat(fare.getFare()).isEqualTo(expectedFare);
    }
}