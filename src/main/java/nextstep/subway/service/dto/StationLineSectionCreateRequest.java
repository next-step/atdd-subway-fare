package nextstep.subway.service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StationLineSectionCreateRequest {
	private Long upStationId;
	private Long downStationId;
	private BigDecimal distance;
	private Long duration;
}
