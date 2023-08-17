package nextstep.api.acceptance.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import static nextstep.api.acceptance.auth.AuthSteps.일반_로그인_성공;
import static nextstep.api.acceptance.member.MemberSteps.회원_생성_성공;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.api.acceptance.AcceptanceTest;
import nextstep.api.acceptance.subway.line.LineSteps;
import nextstep.api.acceptance.subway.station.StationSteps;
import nextstep.api.subway.domain.path.PathSelection;

@DisplayName("지하철 경로 관리 기능")
class FareAcceptanceTest extends AcceptanceTest {

    private Long 교대역, 강남역, 양재역;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        교대역 = StationSteps.지하철역_생성_성공("교대역").getId();
        강남역 = StationSteps.지하철역_생성_성공("강남역").getId();
        양재역 = StationSteps.지하철역_생성_성공("양재역").getId();
    }

    @Nested
    class 운임거리에_따른_추가요금을_적용한다 {

        @ParameterizedTest
        @CsvSource(value = {"0,0", "1,1250", "10,1250"})
        void 거리가_10km_이하인_경우(final int distance, final long expectedFare) {
            // given
            LineSteps.지하철노선_생성_성공(교대역, 강남역, distance, 10, 0);

            // when
            final var response = PathSteps.최단경로조회_성공(교대역, 강남역, PathSelection.DISTANCE);

            // then
            assertThat(response.getFare()).isEqualTo(expectedFare);
        }

        @ParameterizedTest
        @CsvSource(value = {"11,1350", "15,1350", "16,1450", "50,2050"})
        void 거리가_10km_초과_50km_이하인_경우(final int distance, final long expectedFare) {
            // given
            LineSteps.지하철노선_생성_성공(교대역, 강남역, distance, 10, 0);

            // when
            final var response = PathSteps.최단경로조회_성공(교대역, 강남역, PathSelection.DISTANCE);

            // then
            assertThat(response.getFare()).isEqualTo(expectedFare);
        }

        @ParameterizedTest
        @CsvSource(value = {"51,2150", "58,2150", "59,2250"})
        void 거리가_50km_초과인_경우(final int distance, final long expectedFare) {
            // given
            LineSteps.지하철노선_생성_성공(교대역, 강남역, distance, 10, 0);

            // when
            final var response = PathSteps.최단경로조회_성공(교대역, 강남역, PathSelection.DISTANCE);

            // then
            assertThat(response.getFare()).isEqualTo(expectedFare);
        }
    }

    @Test
    void 최단경로중_추가요금이_있는_노선을_환승할_경우_가장_높은_금액의_추가요금을_적용한다() {
        // given
        LineSteps.지하철노선_생성_성공(교대역, 강남역, 5, 10, 10);
        LineSteps.지하철노선_생성_성공(강남역, 양재역, 5, 5, 5);

        // when
        final var response = PathSteps.최단경로조회_성공(교대역, 양재역, PathSelection.DISTANCE);

        // then
        assertThat(response.getFare()).isEqualTo(1260);
    }


    @Nested
    class 연령에_따른_할인정책을_적용한다 {
        private final String email = "user@email.com";
        private final String password = "password";

        @ParameterizedTest
        @ValueSource(ints = {13, 18})
        void 로그인_사용자가_청소년인_경우(final int age) {
            assert (13 <= age && age < 19);

            // given
            LineSteps.지하철노선_생성_성공(교대역, 강남역, 10, 10, 0);

            // given
            회원_생성_성공(email, password, age);
            final var token = 일반_로그인_성공(email, password).getAccessToken();

            // when
            final var response = PathSteps.최단경로조회_성공(token, 교대역, 강남역, PathSelection.DISTANCE);

            // then
            assertThat(response.getFare()).isEqualTo(1070);
        }

        @ParameterizedTest
        @ValueSource(ints = {6, 12})
        void 로그인_사용자가_어린이인_경우(final int age) {
            assert (6 <= age && age < 13);

            // given
            LineSteps.지하철노선_생성_성공(교대역, 강남역, 10, 10, 0);

            // given
            회원_생성_성공(email, password, age);
            final var token = 일반_로그인_성공(email, password).getAccessToken();

            // when
            final var response = PathSteps.최단경로조회_성공(token, 교대역, 강남역, PathSelection.DISTANCE);

            // then
            assertThat(response.getFare()).isEqualTo(800);
        }
    }
}
