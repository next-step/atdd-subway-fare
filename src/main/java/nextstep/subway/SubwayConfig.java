package nextstep.subway;

import nextstep.subway.ui.RequestPathTypeArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class SubwayConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new RequestPathTypeArgumentResolver());
    }
}
