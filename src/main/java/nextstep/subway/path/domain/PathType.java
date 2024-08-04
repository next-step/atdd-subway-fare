package nextstep.subway.path.domain;

import nextstep.subway.line.domain.LineSection;

public enum PathType {
  DISTANCE {
    @Override
    public int getEdgeWeight(LineSection lineSection) {
      return lineSection.getDistance();
    }
  },
  DURATION {
    @Override
    public int getEdgeWeight(LineSection lineSection) {
      return lineSection.getDuration();
    }
  };

  public abstract int getEdgeWeight(LineSection lineSection);
}
