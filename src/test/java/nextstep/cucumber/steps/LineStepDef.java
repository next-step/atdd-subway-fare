package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.SharedContext;
import nextstep.subway.presentation.LineRequest;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

public class LineStepDef implements En {
    private final SharedContext sharedContext;
    ExtractableResponse<Response> response;

    public LineStepDef(SharedContext sharedContext) {
        this.sharedContext = sharedContext;

        Given("다음과 같은 지하철 노선들을 생성하고", (DataTable dataTable) -> {
            List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
            for (Map<String, String> row : rows) {
                String lineName = row.get("노선명");
                String upStation = row.get("상행역");
                String downStation = row.get("하행역");
                int distance = Integer.parseInt(row.get("거리"));
                int duration = Integer.parseInt(row.get("시간"));
                int extraFare = Integer.parseInt(row.get("추가요금"));
                String color = row.get("색상");

                LineRequest lineRequest = new LineRequest(
                        lineName,
                        color,
                        sharedContext.getStationId(upStation),
                        sharedContext.getStationId(downStation),
                        distance,
                        duration,
                        extraFare
                );
                지하철_노선_생성(lineRequest);
            }
        });

        Given("{string} 노선을 생성 하고", (String lineName, DataTable dataTable) -> {
            List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
            for (Map<String, String> row : rows) {
                String upStation = row.get("상행역");
                String downStation = row.get("하행역");
                int distance = Integer.parseInt(row.get("거리"));
                int duration = Integer.parseInt(row.get("시간"));
                int extraFare = Integer.parseInt(row.get("추가요금"));
                String color = row.get("색상");

                LineRequest lineRequest = new LineRequest(
                        lineName,
                        color,
                        sharedContext.getStationId(upStation),
                        sharedContext.getStationId(downStation),
                        distance,
                        duration,
                        extraFare
                );
                지하철_노선_생성(lineRequest);
            }
        });
    }

    private void 지하철_노선_생성(LineRequest lineRequest) {
        response = RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        Long lineId = response.jsonPath().getLong("id");
        sharedContext.addLineId(lineRequest.getName(), lineId);
    }
}
