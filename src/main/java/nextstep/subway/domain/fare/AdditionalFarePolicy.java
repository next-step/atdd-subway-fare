package nextstep.subway.domain.fare;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;

import javax.naming.OperationNotSupportedException;
import java.util.Comparator;
import java.util.List;

public class AdditionalFarePolicy implements FarePolicy {
    @Override
    public long calculateOverFare(Sections sections) {
        List<Section> findSections = sections.getSections();
        return findSections.stream()
                .map(s -> s.getLine())
                .distinct()
                .map(l -> l.getFare().getValue())
                .max(Comparator.naturalOrder())
                .orElse(0);
    }
}
