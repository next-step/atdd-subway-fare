package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.favorite.acceptance.FavoriteSteps.*;
import static nextstep.line.LineTestField.*;
import static nextstep.line.acceptance.LineRequester.createLineThenReturnId;
import static nextstep.member.MemberTestField.*;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.station.acceptance.StationRequester.createStationThenReturnId;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String NOT_AVAILABLE_TOKEN = "notAvailableToken";

    private Long 강남역;
    private Long 선릉역;
    private Long 수원역;
    private Long 노원역;
    private Long 대림역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_추가_식별값_리턴(GANGNAM_STATION_NAME);
        선릉역 = 지하철역_추가_식별값_리턴(SEOLLEUNG_STATION_NAME);
        수원역 = 지하철역_추가_식별값_리턴(SUWON_STATION_NAME);
        노원역 = 지하철역_추가_식별값_리턴(NOWON_STATION_NAME);
        대림역 = 지하철역_추가_식별값_리턴(DEARIM_STATION_NAME);
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성_요청(EMAIL2, PASSWORD, AGE);
    }

    @DisplayName("경로가 정상일경우 즐겨찾기가 등록된다.")
    @Test
    void 즐겨찾기등록() {
        // given
        지하철노선_생성_후_식별값_리턴(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 2, 4);
        지하철노선_생성_후_식별값_리턴(TWO_LINE_NAME, TWO_LINE_COLOR, 선릉역, 수원역, 3, 4);
        지하철노선_생성_후_식별값_리턴(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 4);

        String accessToken = 로그인요청(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_등록(accessToken, 강남역, 선릉역);

        // then
        즐겨찾기등록_응답값_검증(response);
    }

    @DisplayName("유효하지 않은 토큰으로 즐겨찾기 등록시 실패한다.")
    @Test
    void 즐겨찾기등록_유효하지않은토큰() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_등록(NOT_AVAILABLE_TOKEN, 강남역, 선릉역);

        // then
        즐겨찾기등록_유효하지않은토큰_응답값_검증(response);
    }

    @DisplayName("경로에 포함되지 않은 역을 즐겨찾기로 등록할 경우 에러를 던진다")
    @Test
    void 즐겨찾기등록_경로미존재() {
        // given
        지하철노선_생성_후_식별값_리턴(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 2, 4);
        지하철노선_생성_후_식별값_리턴(TWO_LINE_NAME, TWO_LINE_COLOR, 선릉역, 수원역, 3, 4);
        지하철노선_생성_후_식별값_리턴(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 4);

        String accessToken = 로그인요청(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_등록(accessToken, 강남역, 대림역);

        // then
        즐겨찾기등록_경로미존재_응답값_검증(response);
    }

    @DisplayName("동일한 역을 즐겨찾기로 등록할 경우 에러를 던진다")
    @Test
    void 즐겨찾기등록_역동일() {
        // given
        지하철노선_생성_후_식별값_리턴(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 2, 4);
        지하철노선_생성_후_식별값_리턴(TWO_LINE_NAME, TWO_LINE_COLOR, 선릉역, 수원역, 3, 4);
        지하철노선_생성_후_식별값_리턴(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 4);

        String accessToken = 로그인요청(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_등록(accessToken, 강남역, 강남역);

        // then
        즐겨찾기등록_역동일_응답값_검증(response);
    }

    @DisplayName("즐겨찾기 경로 추가 후 즐겨찾기 조회시 해당 경로가 조회되야 한다.")
    @Test
    void 즐겨찾기조회() {
        // given
        지하철노선_생성_후_식별값_리턴(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 2, 4);
        지하철노선_생성_후_식별값_리턴(TWO_LINE_NAME, TWO_LINE_COLOR, 선릉역, 수원역, 3, 4);
        지하철노선_생성_후_식별값_리턴(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 4);

        String accessToken = 로그인요청(EMAIL, PASSWORD);
        즐겨찾기_등록(accessToken, 강남역, 선릉역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회(accessToken);

        // then
        즐겨찾기조회_응답값_검증(response);
    }

    @DisplayName("즐겨찾기 경로 추가 후 즐겨찾기 조회시 유효하지 않은 토큰일경우 조회에 실패한다.")
    @Test
    void 즐겨찾기조회_유효하지않은토큰() {
        // given
        지하철노선_생성_후_식별값_리턴(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 2, 4);
        지하철노선_생성_후_식별값_리턴(TWO_LINE_NAME, TWO_LINE_COLOR, 선릉역, 수원역, 3, 4);
        지하철노선_생성_후_식별값_리턴(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 4);

        String accessToken = 로그인요청(EMAIL, PASSWORD);
        즐겨찾기_등록(accessToken, 강남역, 선릉역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회(NOT_AVAILABLE_TOKEN);

        // then
        즐겨찾기조회_유효하지않은토큰_응답값_검증(response);
    }

    @DisplayName("즐겨찾기 경로 추가 후 즐겨찾기 삭제시 해당 경로가 삭제되야 한다.")
    @Test
    void 즐겨찾기삭제() {
        // given
        지하철노선_생성_후_식별값_리턴(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 2, 4);
        지하철노선_생성_후_식별값_리턴(TWO_LINE_NAME, TWO_LINE_COLOR, 선릉역, 수원역, 3, 4);
        지하철노선_생성_후_식별값_리턴(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 4);

        String accessToken = 로그인요청(EMAIL, PASSWORD);
        즐겨찾기_등록(accessToken, 강남역, 선릉역);

        // when
        즐겨찾기_삭제(accessToken, 즐겨찾기_ID_조회(accessToken));

        // then
        ExtractableResponse<Response> response = 즐겨찾기_조회(accessToken);
        즐겨찾기삭제_응답값_검증(response);
    }

    @DisplayName("즐겨찾기 경로 추가 후 즐겨찾기 삭제시 유효하지 않은 토큰일경우 삭제가 실패한다.")
    @Test
    void 즐겨찾기삭제_유효하지않은토큰() {
        // given
        지하철노선_생성_후_식별값_리턴(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 2, 4);
        지하철노선_생성_후_식별값_리턴(TWO_LINE_NAME, TWO_LINE_COLOR, 선릉역, 수원역, 3, 4);
        지하철노선_생성_후_식별값_리턴(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 4);

        String accessToken = 로그인요청(EMAIL, PASSWORD);
        즐겨찾기_등록(accessToken, 강남역, 선릉역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제(NOT_AVAILABLE_TOKEN, 즐겨찾기_ID_조회(accessToken));

        // then
        즐겨찾기삭제_유효하지않은토큰_응답값_검증(response);
    }

    @DisplayName("즐겨찾기 경로 추가 후 즐겨찾기 삭제시 다른 사용자일경우 에러를 던진다.")
    @Test
    void 즐겨찾기삭제_다른사용자() {
        // given
        지하철노선_생성_후_식별값_리턴(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, 강남역, 선릉역, 2, 4);
        지하철노선_생성_후_식별값_리턴(TWO_LINE_NAME, TWO_LINE_COLOR, 선릉역, 수원역, 3, 4);
        지하철노선_생성_후_식별값_리턴(THREE_LINE_NAME, TRHEE_LINE_COLOR, 강남역, 노원역, 5, 4);

        String accessToken = 로그인요청(EMAIL, PASSWORD);
        String defferentMemberAccessToken = 로그인요청(EMAIL2, PASSWORD);
        즐겨찾기_등록(accessToken, 강남역, 선릉역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제(defferentMemberAccessToken, 즐겨찾기_ID_조회(accessToken));

        // then
        즐겨찾기삭제_다른사용자_응답값_검증(response);
    }

    private Long 지하철역_추가_식별값_리턴(String stationName) {
        return createStationThenReturnId(stationName);
    }

    private Long 지하철노선_생성_후_식별값_리턴(String name, String color, Long upStationId, Long downStationId, int distance, int duration) {
        return createLineThenReturnId(name, color, upStationId, downStationId, distance, duration);
    }

    private void 즐겨찾기등록_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기등록_유효하지않은토큰_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.asString()).isEqualTo("인증에 실패했습니다.");
    }

    private void 즐겨찾기등록_경로미존재_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    private void 즐겨찾기등록_역동일_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 즐겨찾기조회_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getObject("[0].source.name", String.class)).isEqualTo(GANGNAM_STATION_NAME);
        assertThat(response.jsonPath().getObject("[0].target.name", String.class)).isEqualTo(SEOLLEUNG_STATION_NAME);
    }

    private void 즐겨찾기조회_유효하지않은토큰_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.asString()).isEqualTo("인증에 실패했습니다.");
    }

    private void 즐겨찾기삭제_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.jsonPath().getObject("[0].id", Long.class)).isNull();
    }

    private void 즐겨찾기삭제_유효하지않은토큰_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.asString()).isEqualTo("인증에 실패했습니다.");
    }

    private void 즐겨찾기삭제_다른사용자_응답값_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
