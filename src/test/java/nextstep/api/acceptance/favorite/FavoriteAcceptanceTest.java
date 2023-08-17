package nextstep.api.acceptance.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import static nextstep.api.acceptance.AcceptanceHelper.statusCodeShouldBe;
import static nextstep.api.acceptance.auth.AuthSteps.일반_로그인_성공;
import static nextstep.api.acceptance.favorite.FavoriteSteps.즐겨찾기_생성_성공;
import static nextstep.api.acceptance.favorite.FavoriteSteps.즐겨찾기_생성_요청;
import static nextstep.api.acceptance.favorite.FavoriteSteps.즐겨찾기_전체조회_성공;
import static nextstep.api.acceptance.favorite.FavoriteSteps.즐겨찾기_전체조회_요청;
import static nextstep.api.acceptance.favorite.FavoriteSteps.즐겨찾기_제거_성공;
import static nextstep.api.acceptance.favorite.FavoriteSteps.즐겨찾기_제거_요청;
import static nextstep.api.acceptance.member.MemberSteps.회원_생성_성공;
import static nextstep.api.acceptance.subway.line.LineSteps.지하철노선_생성_성공;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import nextstep.api.acceptance.AcceptanceTest;
import nextstep.api.acceptance.subway.station.StationSteps;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private Long 정자역, 판교역, 잠실역;
    private String token;
    private String invalidToken = "invalidToken";

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        prepareSubwayGraph();
        prepareMember();
    }

    private void prepareSubwayGraph() {
        정자역 = StationSteps.지하철역_생성_성공("정자역").getId();
        판교역 = StationSteps.지하철역_생성_성공("판교역").getId();
        지하철노선_생성_성공(정자역, 판교역, 10, 10, 0);

        잠실역 = StationSteps.지하철역_생성_성공("잠실역").getId();
    }

    private void prepareMember() {
        final var email = "user@gmail.com";
        final var password = "password";

        회원_생성_성공(email, password, 20);
        token = 일반_로그인_성공(email, password).getAccessToken();
    }

    @DisplayName("즐겨찾기를 생성한다")
    @Nested
    class createFavorite {

        @Test
        void success() {
            // when
            즐겨찾기_생성_성공(token, 정자역, 판교역);

            // then
            final var response = 즐겨찾기_전체조회_성공(token);
            assertThat(response).hasSize(1);
        }

        @Nested
        class fail {

            @Test
            void 로그인한_상태여야_한다() {
                final var response = 즐겨찾기_생성_요청(invalidToken, 정자역, 판교역);
                statusCodeShouldBe(response, HttpStatus.UNAUTHORIZED);
            }

            @Test
            void 존재하는_상행역이어야_한다() {
                final var response = 즐겨찾기_생성_요청(token, 0L, 판교역);
                statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);
            }

            @Test
            void 존재하는_하행역이어야_한다() {
                final var response = 즐겨찾기_생성_요청(token, 정자역, 0L);
                statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);
            }

            @Test
            void 상행역과_하행역은_연결되어있어야_한다() {
                final var response = 즐겨찾기_생성_요청(token, 잠실역, 판교역);
                statusCodeShouldBe(response, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @DisplayName("즐겨찾기를 조회한다")
    @Nested
    class showFavorites {

        @Test
        void success() {
            // given
            즐겨찾기_생성_성공(token, 정자역, 판교역);

            // when
            final var response = 즐겨찾기_전체조회_성공(token);

            // then
            assertThat(response).hasSize(1);
        }

        @Nested
        class fail {

            @Test
            void 로그인한_상태여야_한다() {
                final var response = 즐겨찾기_전체조회_요청(invalidToken);
                statusCodeShouldBe(response, HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @DisplayName("즐겨찾기 삭제에 성공한다")
    @Nested
    class deleteFavorites {

        @Test
        void success() {
            // given
            즐겨찾기_생성_성공(token, 정자역, 판교역);
            final var favoriteId = 즐겨찾기_전체조회_성공(token).get(0).getId();

            // when
            즐겨찾기_제거_성공(token, favoriteId);

            // then
            final var response = 즐겨찾기_전체조회_성공(token);
            assertThat(response).isEmpty();
        }

        @Nested
        class fail {

            @Test
            void 로그인한_상태여야_한다() {
                // given
                즐겨찾기_생성_성공(token, 정자역, 판교역);
                final var favoriteId = 즐겨찾기_전체조회_성공(token).get(0).getId();

                // when & then
                final var response = 즐겨찾기_제거_요청(invalidToken, favoriteId);
                statusCodeShouldBe(response, HttpStatus.UNAUTHORIZED);
            }
        }
    }
}
