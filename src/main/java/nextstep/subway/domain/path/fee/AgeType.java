package nextstep.subway.domain.path.fee;

import nextstep.auth.application.UserDetails;
import nextstep.member.domain.Member;

public enum AgeType {
    YOUTH {
        @Override
        public Fare calculate(final Fare fare) {
            return fare.minus(Fare.deduction()).discount(20);
        }
    },
    CHILDREN {
        @Override
        public Fare calculate(final Fare fare) {
            return fare.minus(Fare.deduction()).discount(50);
        }
    },
    ANONYMOUS {
        @Override
        public Fare calculate(final Fare fare) {
            return fare;
        }
    };

    public static AgeType of(UserDetails userDetails) {
        if (userDetails.isAnonymous()) {
            return ANONYMOUS;
        }

        final Member member = (Member) userDetails;

        if (member.isChildren()) {
            return CHILDREN;
        }

        if (member.isYouth()) {
            return YOUTH;
        }

        return ANONYMOUS;
    }

    public abstract Fare calculate(final Fare beforeFare);
}
