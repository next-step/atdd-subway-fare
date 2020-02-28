package atdd.member.web;

import atdd.AbstractHttpTest;
import atdd.login.application.dto.LoginResponseView;
import atdd.member.application.dto.MemberResponseView;
import atdd.member.domain.Member;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static atdd.TestUtils.jsonOf;
import static atdd.login.web.LoginController.LOGIN_URL;
import static atdd.member.web.MemberController.MEMBER_URL;


public class MemberHttpTest extends AbstractHttpTest {

    public MemberHttpTest(WebTestClient webTestClient) {
        super(webTestClient);
    }

    public MemberResponseView createMember(Member member) {
        EntityExchangeResult<MemberResponseView> result = createMemberRequest(member);
        return result.getResponseBody();
    }

    public EntityExchangeResult<MemberResponseView> createMemberRequest(Member member) {
        String inputJson = jsonOf(member);
        return createRequest(MemberResponseView.class, MEMBER_URL, inputJson);
    }

    public String loginMember(Member member) {
        EntityExchangeResult<LoginResponseView> result = loginMemberRequest(member);
        LoginResponseView view = result.getResponseBody();
        return view.getTokenType() + " " + view.getAccessToken();
    }

    public EntityExchangeResult<LoginResponseView> loginMemberRequest(Member member) {
        Map<String, Object> map = Map.ofEntries(
                Map.entry("email", member.getEmail()),
                Map.entry("password", member.getPassword())
        );

        String inputJson = jsonOf(map);
        return webTestClient.post().uri(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson), String.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(LoginResponseView.class)
                .returnResult();
    }

}