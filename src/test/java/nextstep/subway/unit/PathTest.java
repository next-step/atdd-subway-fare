package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.subway.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static nextstep.subway.fixtures.MemberFixtures.CHILD_MEMBER_EMAIL;
import static nextstep.subway.fixtures.MemberFixtures.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathTest {

    @Mock
    private Sections sections;

    @Test
    void 어린이_고객일경우_기본거리정책과_어린이요금정책을_가지고_Fare를_생성한다() {
        // given
        when(sections.getMaxExtraFare()).thenReturn(0);
        Member member = new Member(CHILD_MEMBER_EMAIL, PASSWORD, 10);
        Path path = Path.of(sections);

        // when
        Fare fare = path.calculateFare(member);

        // then
        assertThat(fare).extracting("farePolicy")
                .extracting("nextPolicy")
                .isInstanceOf(ChildDiscountFarePolicy.class);
    }

    @Test
    void 청소년_고객일경우_기본거리정책과_청소년요금정책을_가지고_Fare를_생성한다() {
        // given
        when(sections.getMaxExtraFare()).thenReturn(0);
        Member member = new Member(CHILD_MEMBER_EMAIL, PASSWORD, 18);
        Path path = Path.of(sections);

        // when
        Fare fare = path.calculateFare(member);

        // then
        assertThat(fare).extracting("farePolicy")
                .extracting("nextPolicy")
                .isInstanceOf(TeenagerDiscountFarePolicy.class);
    }
}
