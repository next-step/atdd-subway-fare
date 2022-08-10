package nextstep.subway.domain;

import org.springframework.stereotype.Component;

@Component
public class DurationWeightStrategy implements EdgeWeightStrategy {

    @Override
    public int getEdgeWeight(Section section) {
        return section.getDuration();
    }

}
