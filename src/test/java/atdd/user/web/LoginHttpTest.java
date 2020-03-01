package atdd.user.web;

import atdd.user.application.dto.LoginRequestView;
import atdd.user.application.dto.LoginResponseView;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

public class LoginHttpTest {
    public WebTestClient webTestClient;

    public LoginHttpTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public LoginResponseView login(LoginRequestView requestView) {
        return webTestClient.post().uri("/login")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestView), LoginRequestView.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(LoginResponseView.class)
                .getResponseBody()
                .toStream()
                .collect(Collectors.toList())
                .get(0);
    }
}
