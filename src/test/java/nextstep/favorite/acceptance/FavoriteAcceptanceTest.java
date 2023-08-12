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
import static nextstep.member.MemberTestField.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String NOT_AVAILABLE_TOKEN = "notAvailableToken";

    private Station gangnamStation;
    private Station seolleungStation;
    private Station suwonStation;
    private Station nowonStation;
    private Station dearimStation;

    private Member member;
    private Member defferentMember;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        super.setUp();
        gangnamStation = 역저장(GANGNAM_STATION_NAME);
        seolleungStation = 역저장(SEOLLEUNG_STATION_NAME);
        suwonStation = 역저장(SUWON_STATION_NAME);
        nowonStation = 역저장(NOWON_STATION_NAME);
        dearimStation = 역저장(DEARIM_STATION_NAME);
        member = 회원저장(EMAIL, PASSWORD, AGE, ROLE);
        defferentMember = 회원저장(EMAIL2, PASSWORD, AGE, ROLE);
    }

    @DisplayName("경로가 정상일경우 즐겨찾기가 등록된다.")
    @Test
    void 즐겨찾기등록() {
        // given
        노선저장(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, gangnamStation, seolleungStation, 2, 4);
        노선저장(TWO_LINE_NAME, TWO_LINE_COLOR, seolleungStation, suwonStation, 3, 4);
        노선저장(THREE_LINE_NAME, TRHEE_LINE_COLOR, gangnamStation, nowonStation, 5, 4);

        String accessToken = 로그인요청(member);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_등록(accessToken, gangnamStation.getId(), seolleungStation.getId());

        // then
        즐겨찾기등록_응답값_검증(response);
    }

    @DisplayName("유효하지 않은 토큰으로 즐겨찾기 등록시 실패한다.")
    @Test
    void 즐겨찾기등록_유효하지않은토큰() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_등록(NOT_AVAILABLE_TOKEN, gangnamStation.getId(), seolleungStation.getId());

        // then
        즐겨찾기등록_유효하지않은토큰_응답값_검증(response);
    }

    @DisplayName("경로에 포함되지 않은 역을 즐겨찾기로 등록할 경우 에러를 던진다")
    @Test
    void 즐겨찾기등록_경로미존재() {
        // given
        노선저장(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, gangnamStation, seolleungStation, 2, 4);
        노선저장(TWO_LINE_NAME, TWO_LINE_COLOR, seolleungStation, suwonStation, 3, 4);
        노선저장(THREE_LINE_NAME, TRHEE_LINE_COLOR, gangnamStation, nowonStation, 5, 4);

        String accessToken = 로그인요청(member);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_등록(accessToken, gangnamStation.getId(), dearimStation.getId());

        // then
        즐겨찾기등록_경로미존재_응답값_검증(response);
    }

    @DisplayName("동일한 역을 즐겨찾기로 등록할 경우 에러를 던진다")
    @Test
    void 즐겨찾기등록_역동일() {
        // given
        노선저장(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, gangnamStation, seolleungStation, 2, 4);
        노선저장(TWO_LINE_NAME, TWO_LINE_COLOR, seolleungStation, suwonStation, 3, 4);
        노선저장(THREE_LINE_NAME, TRHEE_LINE_COLOR, gangnamStation, nowonStation, 5, 4);

        String accessToken = 로그인요청(member);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_등록(accessToken, gangnamStation.getId(), gangnamStation.getId());

        // then
        즐겨찾기등록_역동일_응답값_검증(response);
    }

    @DisplayName("즐겨찾기 경로 추가 후 즐겨찾기 조회시 해당 경로가 조회되야 한다.")
    @Test
    void 즐겨찾기조회() {
        // given
        노선저장(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, gangnamStation, seolleungStation, 2, 4);
        노선저장(TWO_LINE_NAME, TWO_LINE_COLOR, seolleungStation, suwonStation, 3, 4);
        노선저장(THREE_LINE_NAME, TRHEE_LINE_COLOR, gangnamStation, nowonStation, 5, 4);

        String accessToken = 로그인요청(member);
        즐겨찾기_등록(accessToken, gangnamStation.getId(), seolleungStation.getId());

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회(accessToken);

        // then
        즐겨찾기조회_응답값_검증(response);
    }

    @DisplayName("즐겨찾기 경로 추가 후 즐겨찾기 조회시 유효하지 않은 토큰일경우 조회에 실패한다.")
    @Test
    void 즐겨찾기조회_유효하지않은토큰() {
        // given
        노선저장(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, gangnamStation, seolleungStation, 2, 4);
        노선저장(TWO_LINE_NAME, TWO_LINE_COLOR, seolleungStation, suwonStation, 3, 4);
        노선저장(THREE_LINE_NAME, TRHEE_LINE_COLOR, gangnamStation, nowonStation, 5, 4);

        String accessToken = 로그인요청(member);
        즐겨찾기_등록(accessToken, gangnamStation.getId(), seolleungStation.getId());

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회(NOT_AVAILABLE_TOKEN);

        // then
        즐겨찾기조회_유효하지않은토큰_응답값_검증(response);
    }

    @DisplayName("즐겨찾기 경로 추가 후 즐겨찾기 삭제시 해당 경로가 삭제되야 한다.")
    @Test
    void 즐겨찾기삭제() {
        // given
        노선저장(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, gangnamStation, seolleungStation, 2, 4);
        노선저장(TWO_LINE_NAME, TWO_LINE_COLOR, seolleungStation, suwonStation, 3, 4);
        노선저장(THREE_LINE_NAME, TRHEE_LINE_COLOR, gangnamStation, nowonStation, 5, 4);

        String accessToken = 로그인요청(member);
        즐겨찾기_등록(accessToken, gangnamStation.getId(), seolleungStation.getId());

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
        노선저장(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, gangnamStation, seolleungStation, 2, 4);
        노선저장(TWO_LINE_NAME, TWO_LINE_COLOR, seolleungStation, suwonStation, 3, 4);
        노선저장(THREE_LINE_NAME, TRHEE_LINE_COLOR, gangnamStation, nowonStation, 5, 4);

        String accessToken = 로그인요청(member);
        즐겨찾기_등록(accessToken, gangnamStation.getId(), seolleungStation.getId());

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제(NOT_AVAILABLE_TOKEN, 즐겨찾기_ID_조회(accessToken));

        // then
        즐겨찾기삭제_유효하지않은토큰_응답값_검증(response);
    }

    @DisplayName("즐겨찾기 경로 추가 후 즐겨찾기 삭제시 다른 사용자일경우 에러를 던진다.")
    @Test
    void 즐겨찾기삭제_다른사용자() {
        // given
        노선저장(SHINBUNDANG_LINE_NAME, SHINBUNDANG_LINE_COLOR, gangnamStation, seolleungStation, 2, 4);
        노선저장(TWO_LINE_NAME, TWO_LINE_COLOR, seolleungStation, suwonStation, 3, 4);
        노선저장(THREE_LINE_NAME, TRHEE_LINE_COLOR, gangnamStation, nowonStation, 5, 4);

        String accessToken = 로그인요청(member);
        String defferentMemberAccessToken = 로그인요청(defferentMember);
        즐겨찾기_등록(accessToken, gangnamStation.getId(), seolleungStation.getId());

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제(defferentMemberAccessToken, 즐겨찾기_ID_조회(accessToken));

        // then
        즐겨찾기삭제_다른사용자_응답값_검증(response);
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

    private Station 역저장(String stationName) {
        return stationRepository.save(new Station(stationName));
    }

    private Line 노선저장(String name, String color, Station upStation, Station downStation, int distance, int duration) {
        return lineRepository.save(new Line(name, color, upStation, downStation, distance, duration));
    }

    private Member 회원저장(String email, String password, int age, String role) {
        return memberRepository.save(new Member(email, password, age, role));
    }

}
