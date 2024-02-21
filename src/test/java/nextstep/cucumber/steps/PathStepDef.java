package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import nextstep.cucumber.AcceptanceContext;
import nextstep.exception.ExceptionResponse;
import nextstep.subway.PathSteps;
import nextstep.subway.controller.dto.LineCreateRequest;
import nextstep.subway.controller.dto.LineResponse;
import nextstep.subway.controller.dto.StationCreateRequest;
import nextstep.subway.controller.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class PathStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {
        Given("지하철역들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> rows = table.asMaps();

            for (Map<String, String> columns : rows) {
                String stationName = columns.get("name");
                StationCreateRequest request = new StationCreateRequest(stationName);
                Long id = RestAssured.given().log().all()
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/stations")
                        .then().log().all()
                        .extract()
                        .as(StationResponse.class).getId();
                context.store.put(stationName, id);
            }
        });

        And("지하철 노선들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> rows = table.asMaps();

            for (Map<String, String> columns : rows) {
                LineCreateRequest lineCreateRequest = new LineCreateRequest(
                        columns.get("name"),
                        columns.get("color"),
                        (long) context.store.get(columns.get("upStation")),
                        (long) context.store.get(columns.get("downStation")),
                        Long.parseLong(columns.get("distance"))
                );
                Long lineId = RestAssured.given().log().all()
                        .body(lineCreateRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/lines")
                        .then().log().all()
                        .extract()
                        .as(LineResponse.class).getId();
                context.store.put(columns.get("title"), lineId);
            }
        });

        When("경로 조회시 출발역과 도착역이 같은 경우", () -> {
            String 강남역_ID = String.valueOf(context.store.get("강남역"));
            Map<String, String> params = Map.of("source", 강남역_ID, "target", 강남역_ID);

            // when
            String message = PathSteps.경로_요청을_구성한다()
                    .Response_HTTP_상태_코드(OK.value())
                    .경로_조회_요청을_보낸다(params)
                    .as(ExceptionResponse.class).getMessage();
            context.message = message;
        });

        Then("경로 조회를 할 수 없다", () -> {
            assertThat(context.message).isEqualTo("출발역과 도착역이 같은 경우 경로를 조회할 수 없습니다.");
        });
    }
}
