package nextstep.subway.domain;

import java.util.Arrays;

public enum SearchType {
    DISTANCE("DISTANCE", "거리"),
    DURATION("DURATION", "시간"),
    ;

    private final String type;
    private final String desc;

    SearchType(final String type, final String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static SearchType from(final String type) {
        return Arrays.stream(SearchType.values())
                .filter(searchType -> searchType.name().equals(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 검색 조건을 찾을 수 없습니다."));
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
