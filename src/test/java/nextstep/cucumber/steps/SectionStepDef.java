package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.AcceptanceContext;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.StationResponse;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.subway.utils.steps.SectionSteps.구간_생성_요청;

public class SectionStepDef implements En {
	@Autowired
	private AcceptanceContext context;

	public SectionStepDef() {
		Given("지하철 구간들을 생성하고", (DataTable table) -> {
			table.asMaps().stream()
					.forEach(data -> {
						구간_생성_요청(
								((StationResponse) context.store.get(data.get("downStation"))).getId(),
								((StationResponse) context.store.get(data.get("upStation"))).getId(),
								Integer.parseInt(data.get("distance")),
								((LineResponse) context.store.get(data.get("lineName"))).getId()
						);
					});
		});
	}
}
