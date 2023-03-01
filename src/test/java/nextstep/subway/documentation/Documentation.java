package nextstep.subway.documentation;

import nextstep.SetupSpec;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Documentation extends SetupSpec {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        this.spec.port(port);
    }
}
