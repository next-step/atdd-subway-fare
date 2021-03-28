package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.AddedCostPaymentPolicy;
import nextstep.subway.path.domain.policy.PaymentPolicy;
import nextstep.subway.path.dto.CostRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("AddedCostPaymentPolicy 클래스")
public class AddedCostPaymentPolicyTest extends IsolatePathTest{
    private PaymentPolicy paymentPolicy = new AddedCostPaymentPolicy();

    @Nested
    @DisplayName("cost 메서드는")
    class Describe_cost{
        @Nested
        @DisplayName("경로에 추가요금이 있는 노선이 0개 있는 경우")
        class Context_not_exists_addedCost_line{
            @Test
            @DisplayName("노선별 추가요금이 더해지지 않는 요금이 반환된다.")
            void it_is_original_cost() {
                //when
                CostRequest costRequest = paymentPolicy.cost(CostRequest.of(교대역_강남역_경로));

                //
                assertThat(costRequest.getCost().getCost()).isEqualTo(0);
            }
        }

        @Nested
        @DisplayName("경로에 추가요금이 있는 노선이 1개 있는 경우")
        class Context_with_one_addedCost_line{
            @Test
            @DisplayName("해당 노선의 추가요금이 더해진 요금이 반환된다.")
            void it_is_return_cost_additional_addedCost() {
                //when
                CostRequest costRequest = paymentPolicy.cost(CostRequest.of(교대역_남부터미널역_경로));

                //then
                assertThat(costRequest.getCost().getCost()).isEqualTo(500);
            }
        }

        @Nested
        @DisplayName("경로에 추가요금이 있는 노선이 1개 이상 있는 경우")
        class Context_with_one_more_addedCost_line{
            @Test
            @DisplayName("가장 높은 추가요금이 더해진 요금이 반환된다.")
            void it_is_return_cost_additional_maximum_addedCost() {
                //when
                CostRequest costRequest = paymentPolicy.cost(CostRequest.of(남부터미널역_강남역_경로));

                //then
                assertThat(costRequest.getCost().getCost()).isEqualTo(900);
            }
        }

    }


}
