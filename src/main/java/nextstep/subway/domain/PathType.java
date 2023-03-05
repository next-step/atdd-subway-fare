package nextstep.subway.domain;

public enum PathType {
	DISTANCE,
	DURATION;

	public int getWeight(Section section) {
		if (this.equals(PathType.DISTANCE)) {
			return section.getDistance();
		}
		return section.getDuration();
	}
}
