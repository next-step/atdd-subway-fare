package nextstep.subway.cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.cucumber.AcceptanceContext;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.testhelper.JsonPathHelper;
import nextstep.subway.testhelper.apicaller.StationApiCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationStepDef implements En {
    ExtractableResponse<Response> response;
    @Autowired
    private AcceptanceContext context;

    public StationStepDef() {
        Given("{string} 지하철역을 생성 요청하고", (String name) -> {
            Map<String, String> params = new HashMap<>();
            params.put("name", name);
            StationApiCaller.지하철_역_생성(params);
        });

        Given("지하철역들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> maps = table.asMaps();
            maps.forEach(params -> {
                        ExtractableResponse<Response> response = StationApiCaller.지하철_역_생성(params);
                        context.store.put(params.get("name"), (new ObjectMapper()).convertValue(response.jsonPath().get(), StationResponse.class));
            });
        });

        When("지하철역을 생성하면", () -> {
            Map<String, String> params = new HashMap<>();
            params.put("name", "강남역");
            response = StationApiCaller.지하철_역_생성(params);
        });

        Then("지하철역이 생성된다", () -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });

        Then("지하철역 목록 조회 시 생성한 역을 찾을 수 있다", () -> {
            List<String> stationNames = JsonPathHelper.getAll(StationApiCaller.지하철_역들_조회(),"name", String.class);
            assertThat(stationNames).containsAnyOf("강남역");
        });
    }

}
