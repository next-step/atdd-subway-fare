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
import org.springframework.http.HttpStatus;

import static nextstep.subway.utils.steps.LineSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LineStepDef implements En {
	@Autowired
	private AcceptanceContext context;

	public LineStepDef() {
		Given("지하철 노선을 생성하고", (DataTable table) ->
				table.asMaps().stream()
					.forEach(data -> {
						ExtractableResponse<Response> response = 노선_생성_요청(
								data.get("name"),
								data.get("color"),
								((StationResponse) context.store.get(data.get("startStation"))).getId(),
								((StationResponse) context.store.get(data.get("endStation"))).getId(),
								Integer.parseInt(data.get("distance")),
								Integer.parseInt(data.get("duration"))
						);
						context.store.put(data.get("name"), (new ObjectMapper()).convertValue(response.jsonPath().get(), LineResponse.class));
					})
		);

		When("지하철 노선 목록을 조회하면", () -> context.response = 노선_전체_조회_요청());

		When("생성한 {string} 노선을 조회하면", (String lineName) -> {
			Long id = ((LineResponse) context.store.get(lineName)).getId();
			context.response = 노선_단건_조회_요청(id);
		});

		Then("조회한 지하철 노선 목록에서 생성한 {string} 노선을 찾을 수 있다", (String lineName) -> {
			String[] names = lineName.split(",");

			assertThat(context.response.statusCode()).isEqualTo(HttpStatus.OK.value());
			assertThat(context.response.jsonPath().getList("name")).containsExactly(names);
		});

		Then("생성한 {string} 노선의 정보가 조회된다", (String lineName) -> {
			LineResponse lineResponse = (LineResponse) context.store.get(lineName);

			assertThat(context.response.jsonPath().getString("name")).isEqualTo(lineResponse.getName());
			assertThat(context.response.jsonPath().getString("color")).isEqualTo(lineResponse.getColor());
		});
	}

}
