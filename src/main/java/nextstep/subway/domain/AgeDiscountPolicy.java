package nextstep.subway.domain;

public enum AgeDiscountPolicy {
    BABY {
        @Override
        public int discount(int fare) {
            return 0;
        }
    },
    CHILDREN {
        @Override
        public int discount(int fare) {
            return (int) ((fare - 350) * 0.5);
        }
    },
    TEENAGER {
        @Override
        public int discount(int fare) {
            return (int) ((fare - 350) * 0.8);
        }
    },
    ADULT {
        @Override
        public int discount(int fare) {
            return fare;
        }
    };

    public static final String INVALID_AGE_MESSAGE = "It starts from 1 year old in Korean age.";

    public static AgeDiscountPolicy of(int age) {
        if (age <= 0) {
            throw new IllegalArgumentException(INVALID_AGE_MESSAGE);
        }
        if (age < 6) {
            return BABY;
        }
        if (age < 13) {
            return CHILDREN;
        }
        if (age < 19) {
            return TEENAGER;
        }
        return ADULT;
    }

    public abstract int discount(int fare);
}
