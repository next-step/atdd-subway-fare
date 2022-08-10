package nextstep.subway.domain;

public enum ShortestPathType {
    DISTANCE, DURATION;

    public static ShortestPathType from(String type) {
        if (type == null) {
            throw new IllegalArgumentException("파라미터 type 이 null 입니다.");
        }

        for (ShortestPathType shortestPathType : ShortestPathType.values()) {
            String shortestPathTypeLowerCaseName = shortestPathType.name().toLowerCase();
            if (shortestPathTypeLowerCaseName.equals(type)) {
                return shortestPathType;
            }
        }
        throw new IllegalArgumentException("파라미터 type이 잘못 전달 되었습니다 : " + type);
    }
}
