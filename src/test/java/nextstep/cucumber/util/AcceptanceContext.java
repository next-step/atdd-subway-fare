package nextstep.cucumber.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Profile("test")
@Component
public class AcceptanceContext {
    public Map<String, Object> 저장소 = new HashMap<>();
    public ExtractableResponse<Response> 저장된_응답;

    public void 저장소_숫자_추가하기(String 키, Long 값) {
        저장소.put(키, 값);
    }

    public void 저장소_정보_추가하기(String 키, Object 정보) {
        저장소.put(키, 정보);
    }

    public Long 저장소_숫자_가져오기(String 키) {
        return (Long) 저장소.get(키);
    }

    public Object 저장소_정보_가져오기(String 키) {
        return (Object) 저장소.get(키);
    }
}
