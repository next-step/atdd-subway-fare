package nextstep.subway.domain;

import java.util.Arrays;

public enum SearchType {
    DISTANCE,
    DURATION,
    ;

    public static SearchType from(final String type) {
        return Arrays.stream(SearchType.values())
                .filter(searchType -> searchType.name().equals(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 검색 조건을 찾을 수 없습니다."));
    }
}
