package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.AgePaymentPolicy;
import nextstep.subway.path.domain.policy.PaymentPolicy;
import nextstep.subway.path.dto.CostRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AgePaymentPolicy 클래스")
public class AgePaymentPolicyTest extends IsolatePathTest {
    private final PaymentPolicy paymentPolicy = new AgePaymentPolicy();

    @Nested
    @DisplayName("cost 메서드는")
    class Describe_cost {
        @Nested
        @DisplayName("회원의 나이가 19세 이상의 성인인 경우")
        class Context_with_age_over_20 {
            @Test
            @DisplayName("요금 할인이 적용되지 않는다.")
            void it_is_return_original_cost() {
                //given
                Cost cost = new Cost(1000);

                //when
                CostRequest request = new CostRequest(강남역_양재역_경로, cost, 19);
                CostRequest result = paymentPolicy.cost(request);

                //then
                assertThat(result.getCost().getCost()).isEqualTo(1000);
            }
        }

        @Nested
        @DisplayName("회원의 나이가 13세 이상 19세 미만의 청소년인 경우")
        class Context_with_age_between_13_and_19{
            @Test
            @DisplayName("운임 350원을 공제한 금액에서 20%를 할인받는다.")
            void it_is_return_discount_20percent_cost() {
                //given
                Cost cost = new Cost(1350);

                //when
                CostRequest request = new CostRequest(강남역_양재역_경로, cost, 18);
                CostRequest result = paymentPolicy.cost(request);

                //then
                assertThat(result.getCost().getCost()).isEqualTo(1150);
            }
        }

        @Nested
        @DisplayName("회원의 나이가 6세 이상 13세 미만의 어린이인 경우")
        class Context_with_age_between_6_and_13{
            @Test
            @DisplayName("운임 350원을 공제한 금액에서 50%를 할인받는다.")
            void it_is_return_discount_50percent_cost() {
                //given
                Cost cost = new Cost(1350);

                //when
                CostRequest request = new CostRequest(강남역_양재역_경로, cost, 6);
                CostRequest result = paymentPolicy.cost(request);

                //then
                assertThat(result.getCost().getCost()).isEqualTo(850);
            }
        }
    }
}
