package nextstep.subway.domain;

import nextstep.subway.domain.fee.FeePolicy;
import nextstep.subway.domain.fee.FiftyKmOverCondition;
import nextstep.subway.domain.fee.FiftyKmUnderCondition;

import java.util.Arrays;
import java.util.List;

public class Path {

    private Sections sections;
    private FeePolicy feePolicy = new FeePolicy(
            Arrays.asList(
                    new FiftyKmUnderCondition(),
                    new FiftyKmOverCondition()
            )
    );


    public Path(Sections sections) {
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extraFee() {
        return feePolicy.calculateFee(extractDistance());
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }


}
