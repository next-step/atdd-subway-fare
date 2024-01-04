package nextstep.cucumber.steps;

import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationStepDef implements En {
    ExtractableResponse<Response> response;

    public StationStepDef() {
        When("지하철역을 생성하면", () -> {
            Map<String, String> params = new HashMap<>();
            params.put("name", "강남역");
            response = RestAssured.given().log().all()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/stations")
                    .then().log().all()
                    .extract();
        });

        Then("지하철역이 생성된다", () -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });

        Then("지하철역 목록 조회 시 생성한 역을 찾을 수 있다", () -> {
            List<String> stationNames =
                    RestAssured.given().log().all()
                            .when().get("/stations")
                            .then().log().all()
                            .extract().jsonPath().getList("name", String.class);
            assertThat(stationNames).containsAnyOf("강남역");
        });
    }

}
