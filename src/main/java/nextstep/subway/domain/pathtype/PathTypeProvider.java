package nextstep.subway.domain.pathtype;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PathTypeProvider {

    private static final List<PathType> pathTypes = new ArrayList<>();

    static {
        pathTypes.add(new DistanceType());
        pathTypes.add(new DurationType());
    }

    public PathType provide(String type) {
        return pathTypes.stream()
                .filter(pathType -> pathType.getType().equals(type))
                .findAny().orElseThrow(IllegalArgumentException::new);
    }
}
