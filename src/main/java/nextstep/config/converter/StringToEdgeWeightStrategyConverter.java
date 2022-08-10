package nextstep.config.converter;

import nextstep.subway.domain.EdgeWeightStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.converter.Converter;

public class StringToEdgeWeightStrategyConverter implements Converter<String, EdgeWeightStrategy> {

    private final ApplicationContext applicationContext;

    public StringToEdgeWeightStrategyConverter(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public EdgeWeightStrategy convert(String source) {
        return applicationContext.getBean(source, EdgeWeightStrategy.class);
    }

}
