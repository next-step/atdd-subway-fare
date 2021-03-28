package nextstep.subway.path.domain;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.application.PaymentPolicyHandler;
import nextstep.subway.path.application.PaymentPolicyHandlerV1;
import nextstep.subway.path.domain.policy.AddedCostPaymentPolicy;
import nextstep.subway.path.domain.policy.AgePaymentPolicy;
import nextstep.subway.path.domain.policy.DistancePaymentPolicy;
import nextstep.subway.path.dto.PathResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.domain.PathType.DISTANCE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PathService 클래스")
public class PathServiceTest extends PathTest{
    private LoginMember adult;
    private LoginMember youth;
    private LoginMember kid;


    @BeforeEach
    void setup() {
        super.setup();
        PaymentPolicyHandler paymentPolicyHandler = new PaymentPolicyHandlerV1()
                .link(new DistancePaymentPolicy())
                .link(new AddedCostPaymentPolicy())
                .link(new AgePaymentPolicy());

        pathService = new PathService(graphService, stationService, paymentPolicyHandler);
        adult = new LoginMember(1L, "adult@email.com", "password", 22);
        youth = new LoginMember(2L, "youth@email.com", "password", 17);
        kid = new LoginMember(3L, "kid@email.com", "password", 9);
    }

    @Nested
    @DisplayName("findPath 메서드는")
    class Describe_findPath {
        @Nested
        @DisplayName("추가요금이 있는 노선을 경유할 경우")
        class Context_with_added_cost_lint{
            @Nested
            @DisplayName("성인이라면")
            class Context_with_adult{
                @DisplayName("할인없이 추가요금이 부과된 요금이 청구된다.")
                @Test
                void it_is_not_discounted_cost() {
                    //when
                    PathResponse response = pathService.findPath(adult, 강남역.getId(), 양재역.getId(), DISTANCE);

                    //then
                    assertThat(response.getCost()).isEqualTo(2150);

                }
            }
            @Nested
            @DisplayName("청소년이라면")
            class Context_with_youth{
                @DisplayName("공제액을 제외한 20% 할인된 요금이 청구된다.")
                @Test
                void it_is_20_percent_discounted_cost() {
                    //when
                    PathResponse response = pathService.findPath(youth, 강남역.getId(), 양재역.getId(), DISTANCE);

                    //then
                    assertThat(response.getCost()).isEqualTo(1790);
                }
            }
            @Nested
            @DisplayName("어린이라면")
            class Context_with_kid{
                @DisplayName("공제액을 제외한 50% 할인된 요금이 청구된다.")
                @Test
                void it_is_50_percent_discounted_cost() {
                    //when
                    PathResponse response = pathService.findPath(kid, 강남역.getId(), 양재역.getId(), DISTANCE);

                    //then
                    assertThat(response.getCost()).isEqualTo(1250);
                }
            }
        }
    }

}
