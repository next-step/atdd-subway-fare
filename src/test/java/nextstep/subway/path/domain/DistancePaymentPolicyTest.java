package nextstep.subway.path.domain;

import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DistancePaymentPolicy 클래스")
public class DistancePaymentPolicyTest extends PathTest {
    private PaymentPolicy paymentPolicy = new DistancePaymentPolicy();
    private SubwayGraph subwayGraph;
    @BeforeEach
    void setup() {
        super.setup();
        pathService = new PathService(graphService, stationService, paymentPolicy);

        subwayGraph = graphService.findGraph(PathType.DISTANCE);
    }

    @DisplayName("cost 메서드는")
    @Nested
    class Describe_cost{
        @Nested
        @DisplayName("경로의 총 거리가 10km 이내일 경우")
        class Context_with_distance_under_10 {
            @DisplayName("기본요금이 부과된다.")
            @Test
            void it_is_default_cost() {
                //given
                Station sourceStation = stationService.findStationById(양재역.getId());
                Station targetStation = stationService.findStationById(강남역.getId());
                PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);

                //when
                Cost cost = paymentPolicy.cost(pathResult);

                //then
                assertThat(cost.getCost()).isEqualTo(PaymentPolicy.DEFAULT_COST + 900);
            }
        }

        @Nested
        @DisplayName("경로의 총 거리가 10km 초과 50km 이하일 경우")
        class Context_with_distance_between_10_and_50 {
            @DisplayName("5키로당 100원의 요금이 부과된다.")
            @Test
            void it_is_additional_cost_100_by_5km() {
                //given
                Station sourceStation = stationService.findStationById(교대역.getId());
                Station targetStation = stationService.findStationById(남부터미널역.getId());
                PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);

                //when
                Cost cost = paymentPolicy.cost(pathResult);

                //then
                assertThat(cost.getCost()).isEqualTo(1950);
            }
        }

        @Nested
        @DisplayName("경로의 총 거리가 50km 초과일 경우")
        class Context_with_distance_exceed_50 {
            @DisplayName("8키로당 100원씩 요금이 부과된다.")
            @Test
            void it_is_additional_cost_100_by_8km() {
                //given
                Station sourceStation = stationService.findStationById(교대역.getId());
                Station targetStation = stationService.findStationById(양재역.getId());
                PathResult pathResult = subwayGraph.findPath(sourceStation, targetStation);

                //when
                Cost cost = paymentPolicy.cost(pathResult);

                //then 59km 1250 + 800 + 200
                assertThat(cost.getCost()).isEqualTo(2750);
            }
        }

    }

}
