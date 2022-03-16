package nextstep.subway.config;

import nextstep.subway.domain.sectiontype.DistanceSectionPathType;
import nextstep.subway.domain.sectiontype.DurationSectionPathType;
import nextstep.subway.domain.sectiontype.SectionPathTypes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class InitBean {

    @Bean
    public SectionPathTypes sectionPathTypes(){
        return new SectionPathTypes(Arrays.asList(
                new DistanceSectionPathType()
                , new DurationSectionPathType()));
    }
}
