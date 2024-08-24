package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.SharedContext;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class MemberStepDef implements En {
    private final SharedContext sharedContext;
    ExtractableResponse<Response> response;

    public MemberStepDef(SharedContext sharedContext) {
        this.sharedContext = sharedContext;

        Given("다음과 같은 사용자들이 생성되어 있고", (DataTable dataTable) -> {
            dataTable.asMaps().forEach(row -> {
                String email = row.get("이메일");
                String password = row.get("비밀번호");
                int age = Integer.parseInt(row.get("나이"));

                회원_생성_요청(email, password, age);
            });
        });
    }

    private void 회원_생성_요청(String email, String password, Integer age) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("age", age + "");

        response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/members")
                .then().log().all().extract();

        String location = response.header("Location");
        String[] parts = location.split("/");
        Long memberId = Long.parseLong(parts[parts.length - 1]);
        sharedContext.addMemberId(email, memberId);
    }
}
