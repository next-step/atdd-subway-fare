package nextstep.subway.domain.fee;

import java.util.List;

public class FeePolicy {

    private List<FeeCondition> feeConditions;

    public FeePolicy(List<FeeCondition> feeConditions) {
        this.feeConditions = feeConditions;
    }

    public int calculateFee(int distance){
        for (FeeCondition feeCondition : feeConditions) {
            if(feeCondition.isInclude(distance)){
                return feeCondition.calculateFee(distance);
            }
        }
        return FeeCondition.DEFAULT_FEE;
    }
}
