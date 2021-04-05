package nextstep.subway.path.enums;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum LineFarePolicy {

    LINE_ONE(1L, 0), LINE_TWO(2L, 100), LINE_THREE(3L, 200), LINE_FOUR(4L, 300);

    private final Long id;
    private final int additionalFare;

    LineFarePolicy(Long id, int additionalFare) {
        this.id = id;
        this.additionalFare = additionalFare;
    }

    public Long getId() {
        return id;
    }

    public int getAdditionalFare() {
        return additionalFare;
    }

    public static final Map<Long, Integer> lineFareMap =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(LineFarePolicy::getId, LineFarePolicy::getAdditionalFare)));

    public static int find(Long id){
        return lineFareMap.get(id);
    }
}
