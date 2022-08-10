package nextstep.subway.constant;

import nextstep.subway.domain.Section;

import java.util.Arrays;
import java.util.function.Function;

public enum SearchType {
    DISTANCE(Section::getDistance),
    DURATION(Section::getDuration)
    ;

    private final Function<Section, Integer> criteria;

    SearchType(Function<Section, Integer> criteria) {
        this.criteria = criteria;
    }

    public int getCriteria(Section section) {
        return this.criteria.apply(section);
    }

    public static SearchType findByName(String name) {
        return Arrays.stream(SearchType.values())
                .filter(s -> s.name().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
