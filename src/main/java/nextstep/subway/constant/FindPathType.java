package nextstep.subway.constant;

public enum FindPathType {
    DISTANCE("DISTANCE", "거리"),
    DURATION("DURATION", "시간");

    private final String type;
    private final String description;

    FindPathType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
