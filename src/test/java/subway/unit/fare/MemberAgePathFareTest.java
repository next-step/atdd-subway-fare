package subway.unit.fare;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.member.domain.Member;
import subway.path.application.dto.PathFareCalculationInfo;
import subway.path.application.fare.MemberAgePathFare;
import subway.path.application.fare.PathFareChain;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("PathFareMemberAge (회원 운임 할인) 단위 테스트")
public class MemberAgePathFareTest {

    private final static long BASE_FARE = 2000;
    private PathFareChain pathFare;

    @BeforeEach
    void beforeEach() {
        MemberAgePathFare memberAgePathFare = new MemberAgePathFare();
        pathFare = PathFareChain.chain(memberAgePathFare);
    }

    /**
     * Given 요금이 있고
     * When 비회원 일 때
     * Then 운임할인 없이 그대로 출력된다.
     */
    @DisplayName("비회원 운임 계산")
    @Test
    void guestFare() {
        // given
        final Member 비회원 = null;
        PathFareCalculationInfo calcInfo = PathFareCalculationInfo.builder()
                .fare(BASE_FARE)
                .memberAge(0L)
                .build();

        // when
        PathFareCalculationInfo calcInfoResponse = pathFare.calculateFare(calcInfo);

        // then
        assertThat(calcInfoResponse.getFare()).isEqualTo(BASE_FARE);
    }

    /**
     * Given 요금이 있고
     * When 어린이 일 때
     * Then 350원 차감 후 50% 운임할인 계산된다.
     */
    @DisplayName("어린이 운임 계산")
    @Test
    void childFare() {
        // given
        final Member 어린이 = Member.builder().age(7).build();
        PathFareCalculationInfo calcInfo = PathFareCalculationInfo.builder()
                .fare(BASE_FARE)
                .memberAge(어린이.getAge())
                .build();

        // when
        PathFareCalculationInfo calcInfoResponse = pathFare.calculateFare(calcInfo);

        // then
        assertThat(calcInfoResponse.getFare()).isEqualTo(825L);
    }

    /**
     * Given 요금이 있고
     * When 청소년 일 때
     * Then 350원 차감 후 20% 운임할인 계산된다.
     */
    @DisplayName("청소년 운임 계산")
    @Test
    void teenageFare() {
        // given
        final Member 청소년 = Member.builder().age(17).build();
        PathFareCalculationInfo calcInfo = PathFareCalculationInfo.builder()
                .fare(BASE_FARE)
                .memberAge(청소년.getAge())
                .build();

        // when
        PathFareCalculationInfo calcInfoResponse = pathFare.calculateFare(calcInfo);

        // then
        assertThat(calcInfoResponse.getFare()).isEqualTo(1320L);

    }

    /**
     * Given 요금이 있고
     * When 회원이지만 할인 해당사항이 없을 때
     * Then 운임할인 없이 그대로 출력된다.
     */
    @DisplayName("일반회원 운임 계산")
    @Test
    void memberFare() {
        // given
        final Member 그외_회원 = Member.builder().age(23).build();
        PathFareCalculationInfo calcInfo = PathFareCalculationInfo.builder()
                .fare(BASE_FARE)
                .memberAge(그외_회원.getAge())
                .build();

        // when
        PathFareCalculationInfo calcInfoResponse = pathFare.calculateFare(calcInfo);

        // then
        assertThat(calcInfoResponse.getFare()).isEqualTo(BASE_FARE);

    }
}
