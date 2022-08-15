package nextstep.subway.domain;

import nextstep.subway.domain.policy.fare.DefaultFare;
import nextstep.subway.domain.policy.fare.ElevenToFiftyExtraFare;
import nextstep.subway.domain.policy.fare.ExtraLineFare;
import nextstep.subway.domain.policy.fare.FareManager;
import nextstep.subway.domain.policy.fare.OverFiftyExtraFare;
import nextstep.subway.domain.policy.discount.ChildrenDiscount;
import nextstep.subway.domain.policy.discount.DiscountManager;
import nextstep.subway.domain.policy.discount.TeenagerDiscount;
import org.junit.jupiter.api.BeforeEach;

public abstract class FarePolicyLoaderTest {

    @BeforeEach
    void setUp() {
        FareManager.clearPolicy();
        FareManager.addPolicy(new DefaultFare());
        FareManager.addPolicy(new ElevenToFiftyExtraFare());
        FareManager.addPolicy(new OverFiftyExtraFare());
        FareManager.addPolicy(new ExtraLineFare());

        DiscountManager.clearPolicy();
        DiscountManager.addPolicy(new ChildrenDiscount());
        DiscountManager.addPolicy(new TeenagerDiscount());
    }
}
