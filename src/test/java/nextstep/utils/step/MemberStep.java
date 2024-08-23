package nextstep.utils.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.MemberRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MemberStep {

    public static void 멤버_생성(MemberRequest memberRequest) {

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value()).extract();
    }
}
