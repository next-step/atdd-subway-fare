package nextstep.auth.authentication.filter;

import nextstep.auth.authentication.AuthenticationToken;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class BasicAuthenticationFilterTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @Test
    void convert() {
        BasicAuthenticationFilter filter = new BasicAuthenticationFilter(null, null, null);

        AuthenticationToken token = filter.convert(createMockRequest());

        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    private MockHttpServletRequest createMockRequest() {
        String encodedBasicAuth = Base64.getEncoder()
                .encodeToString(String.format("%s:%s", EMAIL, PASSWORD).getBytes());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic " + encodedBasicAuth);
        return request;
    }
}