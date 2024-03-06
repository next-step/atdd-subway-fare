package nextstep.cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java8.En;
import io.cucumber.datatable.DataTable;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.subway.utils.steps.LineSteps.노선_생성_요청;

public class LineStepDef implements En {
	@Autowired
	private AcceptanceContext context;

	public LineStepDef() {
		Given("지하철 노선들을 생성하고", (DataTable table) -> {
			table.asMaps().stream()
					.forEach(data -> {
						ExtractableResponse<Response> response = 노선_생성_요청(
								data.get("name"),
								data.get("color"),
								((StationResponse) context.store.get(data.get("startStation"))).getId(),
								((StationResponse) context.store.get(data.get("endStation"))).getId(),
								Integer.parseInt(data.get("distance"))
						);
						context.store.put(data.get("name"), (new ObjectMapper()).convertValue(response.jsonPath().get(), LineResponse.class));
					});
		});
	}


}
