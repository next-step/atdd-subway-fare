package nextstep.subway.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class SubwayMapFactory {
    private final Map<PathType, SubwayMap> subwayMaps = new HashMap<>();

    public SubwayMapFactory(Set<SubwayMap> pathFinders) {
        register(pathFinders);
    }

    public SubwayMap subwayMap(String pathType) {
        return subwayMaps.get(PathType.find(pathType));
    }

    private void register(Set<SubwayMap> pathFinders) {
        pathFinders.forEach(pathFinder -> subwayMaps.put(pathFinder.pathType(), pathFinder));
    }
}
