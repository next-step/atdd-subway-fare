package nextstep.domain.subway;

import nextstep.domain.Section;

public enum PathType {
    DISTANCE("DISTANCE") {public Long getWeight(Section section){return section.getDistance();}},
    DURATION("DURATION") {public Long getWeight(Section section){return section.getDuration();}};

    private final String type;

    PathType(String type) {
        this.type = type;
    }

    public String getType(){
        return type;

    }

    public abstract Long getWeight(Section section);
}
