package nextstep.subway.domain;

public enum Age {

    BABY(5){
        @Override
        int ageFare(final int fare) {
            return 0;
        }
    },
    CHILD(12) {
        @Override
        int ageFare(final int fare) {
            return (int) (fare - ((fare - DEFAULT_DISCOUNT) * CHILD_DISCOUNT_PERCENT));
        }
    },
    TEENAGER(19) {
        @Override
        int ageFare(final int fare) {
            return (int) (fare - ((fare - DEFAULT_DISCOUNT) * TEENAGER_DISCOUNT_PERCENT));
        }
    },
    ADULT(Integer.MAX_VALUE) {
        @Override
        int ageFare(final int fare) {
            return fare;
        }
    };

    private static final int DEFAULT_DISCOUNT = 350;
    private static final double CHILD_DISCOUNT_PERCENT = 0.5;
    public static final double TEENAGER_DISCOUNT_PERCENT = 0.2;

    private final int limit;

    Age(final int limit) {
        this.limit = limit;
    }

    public static Age findAge(final int age) {
        if (age <= BABY.limit) {
            return BABY;
        }

        if (age <= CHILD.limit) {
            return CHILD;
        }

        if (age <= TEENAGER.limit) {
            return TEENAGER;
        }

        return ADULT;
    }

    abstract int ageFare(final int fare);
}
