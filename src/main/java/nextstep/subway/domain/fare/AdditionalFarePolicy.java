package nextstep.subway.domain.fare;

import nextstep.subway.domain.line.Line;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import java.util.List;

@Component
@Priority(2)
public class AdditionalFarePolicy implements FarePolicy {
    @Override
    public boolean support(FarePolicyContext context) {
        return context.getLines() != null && !context.getLines().isEmpty();
    }

    @Override
    public Fare invoke(FarePolicyContext context, Fare fare) {
        List<Line> lines = context.getLines();

        Long highestAdditionalFare = lines.stream()
                .map(Line::getAdditionalFare)
                .max(Long::compare)
                .get();

        fare.addFare(highestAdditionalFare);
        return fare;
    }
}
