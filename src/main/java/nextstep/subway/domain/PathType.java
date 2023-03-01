package nextstep.subway.domain;

public enum PathType {
    시간("DURATION"),
    거리("DISTANCE");

    private String type;

    PathType(String type) {
        this.type = type;
    }

    public static PathType convertType(String type) {
        if (type.equals(시간.getType())) {
            return 시간;
        }

        if (type.equals(거리.getType())) {
            return 거리;
        }

        throw new IllegalArgumentException("유효하지 않는 type입니다.");
    }

    public String getType() {
        return type;
    }
}
