package nextstep.domain.subway;

public enum PathType {
    DISTANCE("DISTANCE"),
    DURATION("DURATION");

    private final String type;

    PathType(String type) {
        this.type = type;
    }

    public String getType(){
        return type;

    }
}
