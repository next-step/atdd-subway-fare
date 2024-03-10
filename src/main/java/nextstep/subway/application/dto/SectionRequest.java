package nextstep.subway.application.dto;

public class SectionRequest {
	private Long lineId;

	private Long downStationId;

	private Long upStationId;

	private int distance;

	private int duration;

	public SectionRequest() {
	}

	public SectionRequest(Long downStationId, Long upStationId, int distance, int duration) {
		this.downStationId = downStationId;
		this.upStationId = upStationId;
		this.distance = distance;
		this.duration = duration;
	}

	public Long getLineId() {
		return lineId;
	}

	public Long getDownStationId() {
		return downStationId;
	}

	public Long getUpStationId() {
		return upStationId;
	}

	public int getDistance() {
		return distance;
	}

	public int getDuration() {
		return duration;
	}
}
