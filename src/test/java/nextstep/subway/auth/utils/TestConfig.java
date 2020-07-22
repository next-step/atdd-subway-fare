package nextstep.subway.auth.utils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@TestConfiguration
public class TestConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(createTestSecurityContextPersistenceInterceptor());
    }

    @Bean
    public TestSecurityContextPersistenceInterceptor createTestSecurityContextPersistenceInterceptor() {
        return new TestSecurityContextPersistenceInterceptor();
    }
}
