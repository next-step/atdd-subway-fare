package subway.path.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PathRetrieveType {
    DISTANCE("DISTANCE", "최단거리"),
    DURATION("DURATION", "최소시간");

    private final String key;
    private final String name;
}
