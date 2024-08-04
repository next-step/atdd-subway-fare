package nextstep.cucumber.steps;

import static nextstep.subway.acceptance.step.LineSteps.지하철_노선_생성_요청;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.application.dto.response.LineResponse;
import nextstep.subway.application.dto.response.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class LineStepDef implements En {

    @Autowired
    private AcceptanceContext context;

    public LineStepDef() {

        Given("지하철 노선들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            maps.forEach(param -> {
                Map<String, Object> params = new HashMap<>();
                params.put("name", param.get("name"));
                params.put("color", param.get("color"));
                params.put("upStationId",
                    context.getValueFromStore(param.get("upStation"), StationResponse.class).getId().toString());
                params.put("downStationId",
                    context.getValueFromStore(param.get("downStation"), StationResponse.class).getId().toString());
                params.put("distance", param.get("distance"));
                params.put("duration", param.get("duration"));
                ExtractableResponse<Response> response = 지하철_노선_생성_요청(params);
                context.store.put(params.get("name").toString(), response.as(LineResponse.class));
            });
        });

    }


}
