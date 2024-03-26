package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.auth.application.dto.TokenRequest;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.utils.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberStepDef implements En {
    @Autowired
    private AcceptanceContext acceptanceContext;

    public MemberStepDef() {
        DataTableType((Map<String, String> entry) -> new MemberRequest(
                        entry.get("이메일"),
                        entry.get("비밀번호"),
                        Integer.parseInt(entry.get("나이"))
                )
        );

        Given("회원을 생성한다", (DataTable datatable) -> {
            List<MemberRequest> memberRequests = datatable.asList(MemberRequest.class);
            for (MemberRequest request : memberRequests) {
                ExtractableResponse<Response> response = RestAssured
                        .given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                        .when().post("/members")
                        .then().log().all().extract();
                putMember(request, response);
            }
        });

        Given("회원이 생성되어 있다", () -> {
            assertThat(findMember("a@gmail.com")).isNotNull();
            assertThat(findMember("b@gmail.com")).isNotNull();
            assertThat(findMember("c@gmail.com")).isNotNull();
        });


        Given("아이디 {string} 비밀번호 {string}인 회원이 로그인한다", (String email, String password) -> {
            String token = RestAssured
                    .given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(new TokenRequest(email, password))
                    .when().post("/login/token")
                    .then().log().all()
                    .extract().jsonPath().getString("accessToken");
            putMemberToken(email, token);
        });
    }

    private void putMember(MemberRequest request, ExtractableResponse<Response> response) {
        MemberResponse responseObj = response.as(MemberResponse.class);
        acceptanceContext.putMember(request.getEmail(), responseObj);
    }

    private MemberResponse findMember(String email) {
        return acceptanceContext.findMember(email);
    }

    private void putMemberToken(String email, String token) {
        acceptanceContext.putMemberToken(email, token);
    }
}
