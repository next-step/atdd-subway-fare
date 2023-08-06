package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.fare.DistanceFareRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DistanceFareRuleTest {
    private static final int 이호선_추가요금 = 0;
    private static final int 기본요금 = 1250;
    private Path path;
    private Station 강남역;
    private Station 교대역;
    private Line 이호선;
    private Member 게스트_사용자;
    private DistanceFareRule distanceFareRule;

    @BeforeEach
    void setUp() {
        이호선 = new Line("이호선", "bg-green-500", 이호선_추가요금);
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        게스트_사용자 = new Member("guest", "password", 0);

        distanceFareRule = new DistanceFareRule();
    }

    @DisplayName("거리별 정책 - 이용 거리가 10km 이내인 경우 기본운임 요금을 반환한다. (추가요금 0원)")
    @Test
    void calculateBasicFare() {
        // given
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, 10, 3);
        path = createPath(교대역_강남역_구간);

        // when
        int surcharge = distanceFareRule.calculateFare(path, 게스트_사용자.getAge(), 기본요금);

        // then
        assertThat(surcharge).isEqualTo(1250);
    }

    @DisplayName("거리별 정책 - 이용 거리가 10km 초과 ~ 50km 이내인 경우 5km 마다 100원의 추가운임이 부과된다.")
    @Test
    void calculateOverFare_Over10_Upto50() {
        // given
        int distance = 16;
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, distance, 3);
        path = createPath(교대역_강남역_구간);

        // when
        int surcharge = distanceFareRule.calculateFare(path, 게스트_사용자.getAge(), 기본요금);

        // then
        assertThat(surcharge).isEqualTo(1450);
    }

    @DisplayName("거리별 정책 - 이용 거리가 50km 초과 시 8km마다 100원의 추가운임이 부과된다.")
    @Test
    void calculateOverFare_Over50() {
        // given
        int distance = 58;
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, distance, 3);
        path = createPath(교대역_강남역_구간);

        // when
        int surcharge = distanceFareRule.calculateFare(path, 게스트_사용자.getAge(), 기본요금);

        // then
        assertThat(surcharge).isEqualTo(2150);
    }

    @DisplayName("요금계산 시 거리가 0보다 작은 경우 에러가 발생한다")
    @Test
    void calculateDistanceFare_InvalidDistance_Exception() {
        // given
        int distance = -1;
        Section 교대역_강남역_구간 = new Section(이호선, 교대역, 강남역, distance, 3);
        path = createPath(교대역_강남역_구간);

        // when, then
        assertThatThrownBy(() -> distanceFareRule.calculateFare(path, 게스트_사용자.getAge(), 기본요금))
                .isInstanceOf(RuntimeException.class)
                .message().isEqualTo("거리가 올바르지 않습니다.");
    }

    private Path createPath(Section... section) {
        Sections sections = new Sections(List.of(section));
        return new Path(sections);
    }
}
