package nextstep.subway.config;

import nextstep.subway.domain.PathType;
import org.springframework.core.convert.converter.Converter;

public class PathTypeConverter implements Converter<String, PathType> {
    @Override
    public PathType convert(String code) {
        return PathType.of(code);
    }
}
