package nextstep.cucumber.steps;

import io.cucumber.java.DataTableType;
import java.util.Map;
import nextstep.subway.station.application.dto.StationRequest;

public class ParameterTypeDefinitions {
  @DataTableType
  public StationRequest stationRequest(Map<String, String> entry) {
    return new StationRequest(entry.get("name"));
  }
}
