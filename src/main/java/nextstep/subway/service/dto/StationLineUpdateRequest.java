package nextstep.subway.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StationLineUpdateRequest {
	private String name;
	private String color;
}
