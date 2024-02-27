package nextstep.subway.cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.cucumber.AcceptanceContext;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.testhelper.apicaller.LineApiCaller;
import nextstep.subway.testhelper.apicaller.MemberApiCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public MemberStepDef() {
        Given("회원가입 요청을 한다", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            maps.stream()
                    .forEach(it -> {
                        Map<String, String> params = new HashMap<>();
                        ExtractableResponse<Response> response = MemberApiCaller.회원_생성_요청(it.get("email"), it.get(
                                "password"), Integer.parseInt(it.get("age")));
                    });
        });

        Given("{string}세의 {string}아이디로 사용자가 로그인을 한다", (String age, String email) -> {
            Map<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("password", "password");
            ExtractableResponse<Response> response = MemberApiCaller.회원_로그인_요청(params);
            context.store.put("token", (new ObjectMapper()).convertValue(response.jsonPath().getString("accessToken"),
                    String.class));
        });
    }
}
