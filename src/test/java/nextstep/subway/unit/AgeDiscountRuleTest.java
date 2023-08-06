package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.fare.AgeDiscountRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AgeDiscountRuleTest {
    private static final int 이호선_추가요금 = 0;
    private static final int 기본요금 = 1250;
    private Path path;
    private Station 강남역;
    private Station 교대역;
    private Line 이호선;
    private Member 청소년_사용자;
    private Member 어린이_사용자;
    private Member 성인_사용자;

    private AgeDiscountRule ageDiscountRule;

    @BeforeEach
    void setUp() {
        이호선 = new Line("이호선", "bg-green-500", 이호선_추가요금);
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");

        청소년_사용자 = new Member("teen", "password", 15);
        어린이_사용자 = new Member("children", "password", 6);
        성인_사용자 = new Member("adult", "", 25);

        ageDiscountRule = new AgeDiscountRule();
    }

    @DisplayName("나이별 정책 - 요금계산 시 로그인 사용자의 연령이 청소년(13세 이상 ~ 19세 미만)인 경우 운임에서 350원을 공제한 금액의 20%를 할인한다")
    @Test
    void calculateAgeFare_Teenager() {
        // given
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, 10, 3);
        path = createPath(교대역_강남역_구간);

        // when
        int fare = ageDiscountRule.calculateFare(path, 청소년_사용자.getAge(), 기본요금);

        // then
        assertThat(fare).isEqualTo(720);
    }

    @DisplayName("나이별 정책 - 요금계산 시 로그인 사용자의 연령이 어린이(6세 이상 ~ 13세 미만)인 경우 운임에서 350원을 공제한 금액의 50%를 할인한다")
    @Test
    void calculateAgeFare_Children() {
        // given
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, 10, 3);
        path = createPath(교대역_강남역_구간);

        // when
        int fare = ageDiscountRule.calculateFare(path, 어린이_사용자.getAge(), 기본요금);

        // then
        assertThat(fare).isEqualTo(450);
    }

    @DisplayName("나이별 정책 - 요금계산 시 로그인 사용자의 연령이 성인(19세 이상)인 경우 할인되지 않는다")
    @Test
    void calculateAgeFare_Adult() {
        // given
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, 10, 3);
        path = createPath(교대역_강남역_구간);

        // when
        int fare = ageDiscountRule.calculateFare(path, 성인_사용자.getAge(), 기본요금);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    private Path createPath(Section... section) {
        Sections sections = new Sections(List.of(section));
        return new Path(sections);
    }
}
