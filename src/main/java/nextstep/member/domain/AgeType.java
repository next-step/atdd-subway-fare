package nextstep.member.domain;

public enum AgeType {
    BABY {
        @Override
        public int discount(int fare) {
            return 0;
        }
    },
    CHILDREN {
        @Override
        public int discount(int fare) {
            return (int) ((fare - BASIC_FARE) * 0.5) + BASIC_FARE;
        }
    },
    TEENAGER {
        @Override
        public int discount(int fare) {
            return (int) ((fare - BASIC_FARE) * 0.8) + BASIC_FARE;
        }
    },
    ADULT {
        @Override
        public int discount(int fare) {
            return fare;
        }
    };

    public static final String INVALID_AGE_MESSAGE = "It starts from 1 year old";
    private static final int BASIC_FARE = 350;

    public static AgeType of(int age) {
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
