package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.FarePolicyRequest;

public class LinePolicy implements FarePolicy{

    private Sections sections;

    private LinePolicy(Sections sections) {
        this.sections = sections;
    }

    public static LinePolicy from(Sections sections) {
        return new LinePolicy(sections);
    }

    @Override
    public int calculate(FarePolicyRequest request) {
        return sections.getLineExtraChargeMax();
    }
}
