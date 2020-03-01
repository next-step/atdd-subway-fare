package atdd.member.web;

import atdd.AbstractHttpTest;
import atdd.member.application.dto.MemberResponseView;
import atdd.member.domain.Member;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static atdd.TestUtils.jsonOf;
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

}