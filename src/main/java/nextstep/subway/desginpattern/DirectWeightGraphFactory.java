package nextstep.subway.desginpattern;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.desginpattern.DirectWeightGraphFactory.DirectWeightGraphType.DISTANCE;
import static nextstep.subway.desginpattern.DirectWeightGraphFactory.DirectWeightGraphType.DURATION;

public class DirectWeightGraphFactory {
    private static Map<DirectWeightGraphType, DirectWeightGraphTemplate> directWeightGraphTemplateMap;

    static {
        directWeightGraphTemplateMap = Arrays.stream(DirectWeightGraphType.values()).collect(
                Collectors.toMap(
                        it -> it,
                        it -> it.getDirectWeightGraphTemplate())
        );
    }

    public static DirectWeightGraphTemplate distance() {
        return factory(DISTANCE.directWeightGraphTypeName);
    }

    public static DirectWeightGraphTemplate duration() {
        return factory(DURATION.directWeightGraphTypeName);
    }

    public static DirectWeightGraphTemplate factory(final String directWeightGraphTypeName) {
        final DirectWeightGraphType directWeightGraphType = DirectWeightGraphType.of(directWeightGraphTypeName);
        return directWeightGraphTemplateMap.get(directWeightGraphType);
    }

    public enum DirectWeightGraphType {
        DISTANCE("distance", new ShortestDistanceGraphTemplate()),
        DURATION("duration", new ShortestDurationGraphTemplate());

        private final String directWeightGraphTypeName;
        private final DirectWeightGraphTemplate directWeightGraphTemplate;

        public static DirectWeightGraphType of(final String directWeightGraphTypeName) {
            return Arrays.stream(values())
                    .filter(it -> it.directWeightGraphTypeName.equals(directWeightGraphTypeName))
                    .findAny()
                    .orElseThrow(IllegalArgumentException::new);
        }

        DirectWeightGraphType(final String directWeightGraphTypeName, final DirectWeightGraphTemplate directWeightGraphTemplate) {
            this.directWeightGraphTypeName = directWeightGraphTypeName;
            this.directWeightGraphTemplate = directWeightGraphTemplate;
        }

        public DirectWeightGraphTemplate getDirectWeightGraphTemplate() {
            return directWeightGraphTemplate;
        }
    }

    private DirectWeightGraphFactory() {
    }
}
