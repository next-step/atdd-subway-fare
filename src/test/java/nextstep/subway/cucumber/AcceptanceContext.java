package nextstep.subway.cucumber;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class AcceptanceContext {

    private Map<String, String> store = new HashMap<>();

}
