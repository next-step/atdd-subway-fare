package nextstep.subway.ui;

import nextstep.subway.domain.SearchType;
import org.springframework.core.convert.converter.Converter;

public class SearchTypeRequestConverter implements Converter<String, SearchType> {

    @Override
    public SearchType convert(String type) {
        return SearchType.valueOf(type);
    }
}
