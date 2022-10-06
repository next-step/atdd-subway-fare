package nextstep;

import nextstep.subway.util.pathfinder.PathType;
import org.springframework.core.convert.converter.Converter;

public class PathTypeConverter implements Converter<String, PathType> {
    @Override
    public PathType convert(String source) {
        return PathType.from(source);
    }
}
