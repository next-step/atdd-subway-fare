package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import nextstep.cucumber.AcceptanceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SectionStepDef implements En {
    public static final String SECTIONS_URL = "/lines/%s/sections";

    @Autowired
    private AcceptanceContext context;

    public SectionStepDef() {
        Given("구간을 등록하고", (DataTable table)-> {
            List<Map<String, String>> maps = table.asMaps();
            for (Map<String, String> map : maps) {
                final Long lineId = (Long) context.store.get(map.get("lineName"));
                final Long upStationId = (Long) context.store.get(map.get("upStation"));
                final Long downStationId = (Long) context.store.get(map.get("downStation"));
                final int distance = Integer.parseInt(map.get("distance"));
                final Map<String, String> params = registerSectionRequestPixture( upStationId, downStationId, distance);

                context.response = RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post(String.format(SECTIONS_URL, lineId))
                        .then().log().all()
                        .extract();
            }
        });
    }

    private static Map<String, String> registerSectionRequestPixture(final Long upStationId, final Long downStationId, final int distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));
        return params;
    }
}
