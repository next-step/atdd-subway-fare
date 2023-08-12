package nextstep.subway.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StationLineCreateRequest {
	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private BigDecimal distance;
	private Long duration;
	private BigDecimal additionalFee;
}
