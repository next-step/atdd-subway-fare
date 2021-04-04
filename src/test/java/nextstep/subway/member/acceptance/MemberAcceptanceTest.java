package nextstep.subway.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.acceptance.documentation.MemberDocumentation;
import nextstep.subway.member.acceptance.documentation.MemberLoginDocumentation;
import nextstep.subway.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.acceptance.MemberRequestSteps.*;
import static nextstep.subway.member.acceptance.MemberVerificationSteps.*;
import static nextstep.subway.utils.BaseDocumentation.givenDefault;

public class MemberAcceptanceTest extends AcceptanceTest {

    private static final String DOCUMENT_IDENTIFIER_MEMBER = "member/{method-name}";

    private static final String NEW_EMAIL = "newemail@email.com";
    private static final String NEW_PASSWORD = "newpassword";
    private static final int NEW_AGE = 21;

    @DisplayName("회원가입을 한다.")
    @Test
    void registerMember() {
        // given
        baseDocumentation = new MemberDocumentation(spec);
        RequestSpecification 사용자_회원가입_문서화_요청 = baseDocumentation.requestDocumentOfAllType(DOCUMENT_IDENTIFIER_MEMBER);

        // when
        ExtractableResponse<Response> response = 회원_생성_요청(사용자_회원가입_문서화_요청, ADULT_EMAIL, PASSWORD, ADULT_AGE);

        // then
        회원_생성_됨(response);
    }

    @Test
    @DisplayName("로그인을 한다.")
    void loginMember() {
        // given
        baseDocumentation = new MemberLoginDocumentation(spec);
        RequestSpecification 사용자_로그인_문서화_요청 = baseDocumentation.requestDocumentOfAllType(DOCUMENT_IDENTIFIER_MEMBER);
        회원_생성_요청(givenDefault(), ADULT_EMAIL, PASSWORD, ADULT_AGE);

        // when
        TokenResponse tokenResponse = 로그인_되어_있음(사용자_로그인_문서화_요청, ADULT_EMAIL, PASSWORD);

        // then
        회원_로그인_됨(tokenResponse);
    }

    @DisplayName("회원 정보를 조회한다.")
    @Test
    void getMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(givenDefault(), ADULT_EMAIL, PASSWORD, ADULT_AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회_됨(response, ADULT_EMAIL, ADULT_AGE);

    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(givenDefault(), ADULT_EMAIL, PASSWORD, ADULT_AGE);

        // when
        ExtractableResponse<Response> response = 회원_정보_수정_요청(createResponse, NEW_EMAIL, NEW_PASSWORD, ADULT_AGE);

        // then
        회원_정보_수정_됨(response);
    }

    @DisplayName("회원 정보를 삭제한다.")
    @Test
    void deleteMember() {
        // given
        ExtractableResponse<Response> createResponse = 회원_생성_요청(givenDefault(), ADULT_EMAIL, PASSWORD, ADULT_AGE);

        // when
        ExtractableResponse<Response> response = 회원_삭제_요청(createResponse);

        // then
        회원_삭제_됨(response);
    }

    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createdMemberResponse = 회원_생성_요청(givenDefault(), ADULT_EMAIL, PASSWORD, ADULT_AGE);

        // then
        회원_생성_됨(createdMemberResponse);

        // when
        ExtractableResponse<Response> foundMemberResponse = 회원_정보_조회_요청(createdMemberResponse);

        // then
        회원_정보_조회_됨(foundMemberResponse, ADULT_EMAIL, ADULT_AGE);

        // when
        ExtractableResponse<Response> updatedMemberResponse = 회원_정보_수정_요청(createdMemberResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        회원_정보_수정_됨(updatedMemberResponse);

        // when
        ExtractableResponse<Response> deletedMemberResponse = 회원_삭제_요청(createdMemberResponse);

        // then
        회원_삭제_됨(deletedMemberResponse);
    }

    @Test
    @DisplayName("나의 정보를 조회한다.")
    void findMemberOfMine() {
        // given
        baseDocumentation = new MemberDocumentation(spec);
        RequestSpecification 회원_정보_조회_문서화_요청 = baseDocumentation.requestDocumentOfFind(DOCUMENT_IDENTIFIER_MEMBER);
        회원_생성_요청(givenDefault(), ADULT_EMAIL, PASSWORD, ADULT_AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(ADULT_EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(회원_정보_조회_문서화_요청, tokenResponse);

        // then
        회원_정보_조회_됨(response, ADULT_EMAIL, ADULT_AGE);
    }

    @Test
    @DisplayName("나의 정보를 수정한다.")
    void updateMemberOfMine() {
        // given
        회원_생성_요청(givenDefault(), ADULT_EMAIL, PASSWORD, ADULT_AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(ADULT_EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 내_회원_정보_수정_요청(tokenResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        회원_정보_수정_됨(response);
        회원_정보_조회_됨(response, NEW_EMAIL, NEW_AGE);
    }

    @Test
    @DisplayName("나의 정보를 삭제한다.")
    void deleteMemberOfMine() {
        // given
        회원_생성_요청(givenDefault(), ADULT_EMAIL, PASSWORD, ADULT_AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(ADULT_EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = 내_회원_정보_삭제_요청(tokenResponse);

        // then
        회원_삭제_됨(response);
    }

    @Test
    @DisplayName("나의 정보를 관리한다.")
    void manageMyInfo() {
        // when
        ExtractableResponse<Response> createdMemberResponse = 회원_생성_요청(givenDefault(), ADULT_EMAIL, PASSWORD, ADULT_AGE);

        // then
        회원_생성_됨(createdMemberResponse);
        TokenResponse tokenResponse = 로그인_되어_있음(ADULT_EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> foundMemberResponse = 내_회원_정보_조회_요청(givenDefault(), tokenResponse);

        // then
        회원_정보_조회_됨(foundMemberResponse, ADULT_EMAIL, ADULT_AGE);

        // when
        ExtractableResponse<Response> updatedMemberResponse = 내_회원_정보_수정_요청(tokenResponse, NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        회원_정보_수정_됨(updatedMemberResponse);

        // when
        ExtractableResponse<Response> deletedMemberResponse = 내_회원_정보_삭제_요청(tokenResponse);

        // then
        회원_삭제_됨(deletedMemberResponse);
    }
}
