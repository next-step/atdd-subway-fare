package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.application.dto.LineResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineStepDef implements En {
    public static final String LINES_URL = "/lines";

    @Autowired
    private AcceptanceContext context;

    public LineStepDef() {
        Given("노선들을 생성 요청하고", (DataTable table)-> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> map : maps) {
                final String name = map.get("name");
                final String color = map.get("color");
                final Long upStationId = (Long) context.store.get(map.get("upStation"));
                final Long downStationId = (Long) context.store.get(map.get("downStation"));
                final int distance = Integer.parseInt(map.get("distance"));
                final Map<String, String> params = createLineRequestPixture(name, color, upStationId, downStationId, distance);

                final Long lineId = RestAssured
                        .given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post(LINES_URL)
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .as(LineResponse.class).getId();
                context.store.put(name, lineId);
            }
        });
    }

    public static Map<String, String> createLineRequestPixture(final String name, final String color,
                                                               final Long upStationId, final Long downStationId, final int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));
        return params;
    }
}
