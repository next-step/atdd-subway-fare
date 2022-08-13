package nextstep.subway.payment;

import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Section;

import java.util.List;

public class LinePaymentPolicy implements PaymentPolicy {

    private List<Section> sections;

    public LinePaymentPolicy(List<Section> sections) {
        this.sections = sections;
    }

    @Override
    public void calculate(Fare fare) {
        fare.increase(calculateMostExpensiveLine());
    }

    private int calculateMostExpensiveLine() {
        return sections.stream().mapToInt(section -> section.getLine().getFare()).max()
                .orElseThrow(() -> new IllegalStateException("구간 리스트가 비어있습니다."));
    }
}
