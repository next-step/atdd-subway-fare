package nextstep.subway.ui;

import nextstep.subway.domain.SectionCondition;
import org.springframework.core.convert.converter.Converter;

public class PathSearchConditionConverter implements Converter<String, SectionCondition> {
    @Override
    public SectionCondition convert(String type) {
        return SectionCondition.ofType(type);
    }
}
