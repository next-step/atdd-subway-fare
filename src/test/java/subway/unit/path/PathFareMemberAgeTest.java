package subway.unit.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.member.domain.Member;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("PathFareMemberAge (회원 운임 할인) 단위 테스트")
public class PathFareMemberAgeTest {
    private final Member 비회원 = null;
    private final Member 어린이 = Member.builder().age(7).build();
    private final Member 청소년 = Member.builder().age(17).build();
    private final Member 그외_회원 = Member.builder().age(23).build();

    /**
     * Given 요금이 있고
     * When 비회원 일 때
     * Then 운임할인 없이 그대로 출력된다.
     */
    @DisplayName("비회원 운임 계산")
    @Test
    void guestFare() {

    }

    /**
     * Given 요금이 있고
     * When 어린이 일 때
     * Then 350원 차감 후 50% 운임할인 계산된다.
     */
    @DisplayName("어린이 운임 계산")
    @Test
    void childFare() {

    }

    /**
     * Given 요금이 있고
     * When 청소년 일 때
     * Then 350원 차감 후 20% 운임할인 계산된다.
     */
    @DisplayName("청소년 운임 계산")
    @Test
    void teenageFare() {

    }

    /**
     * Given 요금이 있고
     * When 회원이지만 할인 해당사항이 없을 때
     * Then 운임할인 없이 그대로 출력된다.
     */
    @DisplayName("일반회원 운임 계산")
    @Test
    void memberFare() {

    }
}
