package nextstep.cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.controller.dto.LineResponse;
import nextstep.subway.controller.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineStepDef implements En {
    @Autowired
    private AcceptanceContext context;

    public LineStepDef() {
        Given("지하철 노선들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            maps.stream()
                    .forEach(param -> {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", param.get("name"));
                        params.put("color", param.get("color"));
                        params.put("upStationId", ((StationResponse) context.store.get(param.get("upStation"))).getId().toString());
                        params.put("downStationId", ((StationResponse) context.store.get(param.get("downStation"))).getId().toString());
                        params.put("distance", param.get("distance"));
                        params.put("duration", param.get("duration"));
                        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                .body(params)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .when()
                                .post("/lines")
                                .then().log().all()
                                .extract();
                        context.store.put(params.get("name"), (new ObjectMapper()).convertValue(response.jsonPath().get(), LineResponse.class));
                    });
        });

        Given("지하철 구간을 등록 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            maps.stream()
                    .forEach(param -> {
                        String lineName = param.get("lineName");
                        Map<String, String> params = new HashMap<>();
                        params.put("upStationId", ((StationResponse) context.store.get(param.get("upStation"))).getId().toString());
                        params.put("downStationId", ((StationResponse) context.store.get(param.get("downStation"))).getId().toString());
                        params.put("distance", param.get("distance"));
                        params.put("duration", param.get("duration"));
                        LineResponse line = (LineResponse) context.store.get(lineName);
                        RestAssured.given().log().all()
                                .body(params)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .when()
                                .post("/lines/{lineId}/sections", line.getId())
                                .then().log().all();
                    });
        });
    }
}