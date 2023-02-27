package nextstep.subway.applicaion;

public enum PathType {
    시간("DURATION");

    private String type;

    PathType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
