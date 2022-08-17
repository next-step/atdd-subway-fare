package nextstep.subway.util.fare;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Path;

public abstract class FareChain {
    private FareChain nextFareChain;

    protected FareChain() {
    }

    public void connect(FareChain fareChain) {
        this.nextFareChain = fareChain;
    }

    public int calculate(Path path, Member member) {
        if (nextFareChain != null) {
            return nextFareChain.calculate(path, member);
        }
        return 0;
    }
}
