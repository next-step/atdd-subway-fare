package nextstep.subway.unit;

import nextstep.member.domain.Guest;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.service.decorator.component.SurchargeCalculator;
import nextstep.subway.domain.service.decorator.concrete.AgeSurchargeDecorator;
import nextstep.subway.domain.service.decorator.concrete.DistanceSurchargeCalculate;
import nextstep.subway.domain.service.decorator.concrete.LineSurchargeDecorator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FareDecoratorTest {

    Member 어린이;
    Member 청소년;
    Member 성인;
    Member 게스트;
    List<Line> lines;

    @BeforeEach
    void setUp() {
        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");

        Line 신분당선 = new Line("신분당선", "red", 900);
        Line 이호선 = new Line("2호선", "red");
        Line 삼호선 = new Line("3호선", "red", 500);

        신분당선.addSection(강남역, 양재역, 3, 15);
        이호선.addSection(교대역, 강남역, 3, 1);
        삼호선.addSection(교대역, 남부터미널역, 5, 2);
        삼호선.addSection(남부터미널역, 양재역, 5, 4);

        lines = List.of(신분당선, 이호선, 삼호선);

        어린이 = new Member("children", "123", 7);
        청소년 = new Member("teenager", "123", 15);
        성인 = new Member("adult", "123", 20);
        게스트 = new Guest();

    }


    /* given 운임 거리 50
     * and   900원의 추가요금이 부여되는 신분당선이 포함
     * and   500원의 추가요금이 부여되는 삼호선이 포함
     * and   6세 이상 13세 미만에 포함되는 나이
     * when  지하철 요금을 계산한다.
     * then  어린이는 요금에서 350을 공제하고 요금을 50% 할인 받는다.
     * and   노선 중 가장 높은 신분당선의 추가요금을 운임요금에 부여한다.
     * and   계산 결과가 정책과 동일한지 검증한다.
     */
    @Test
    void 어린이_추가요금_검증() {
        SurchargeCalculator fareCalculator = new AgeSurchargeDecorator(
                new LineSurchargeDecorator(new DistanceSurchargeCalculate(50), lines)
                , 어린이);

        int fare = (int) ((3050 - 350) * 0.5);

        assertThat(fareCalculator.appendFare()).isEqualTo(fare);
    }

    /* given 운임 거리 50
     * and   900원의 추가요금이 부여되는 신분당선이 포함
     * and   500원의 추가요금이 부여되는 삼호선이 포함
     * and   13세 이상 19세 미만에 포함되는 나이
     * when  지하철 요금을 계산한다.
     * then  청소년는 요금에서 350을 공제하고 요금을 80% 할인 받는다.
     * and   노선 중 가장 높은 신분당선의 추가요금을 운임요금에 부여한다.
     * and   계산 결과가 정책과 동일한지 검증한다.
     */
    @Test
    void 청소년_추가요금_검증() {
        SurchargeCalculator fareCalculator = new AgeSurchargeDecorator(
                new LineSurchargeDecorator(new DistanceSurchargeCalculate(50), lines)
                , 청소년);

        int fare = (int) ((3050 - 350) * 0.8);

        assertThat(fareCalculator.appendFare()).isEqualTo(fare);
    }

    /* given 운임 거리 50
     * and   900원의 추가요금이 부여되는 신분당선이 포함
     * and   500원의 추가요금이 부여되는 삼호선이 포함
     * and   19세 이상인 나이
     * when  지하철 요금을 계산한다.
     * then   노선 중 가장 높은 신분당선의 추가요금을 운임요금에 부여한다.
     * and   계산 결과가 정책과 동일한지 검증한다.
     */
    @Test
    void 성인_추가요금_검증() {
        SurchargeCalculator fareCalculator = new AgeSurchargeDecorator(
                new LineSurchargeDecorator(new DistanceSurchargeCalculate(50), lines)
                , 성인);

        int fare = 3050;

        assertThat(fareCalculator.appendFare()).isEqualTo(fare);
    }

    /* given 운임 거리 50
     * and   900원의 추가요금이 부여되는 신분당선이 포함
     * and   500원의 추가요금이 부여되는 삼호선이 포함
     * and   게스트는 할인 정책을 적용하지 않는다.
     * when  지하철 요금을 계산한다.
     * then  노선 중 가장 높은 신분당선의 추가요금을 운임요금에 부여한다.
     * and   계산 결과가 정책과 동일한지 검증한다.
     */
    @Test
    void 게스트_추가요금_검증() {
        SurchargeCalculator fareCalculator = new AgeSurchargeDecorator(
                new LineSurchargeDecorator(new DistanceSurchargeCalculate(50), lines)
                , 게스트);

        int fare = 3050;

        assertThat(fareCalculator.appendFare()).isEqualTo(fare);
    }
}
