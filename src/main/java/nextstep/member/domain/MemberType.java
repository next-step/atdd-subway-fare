package nextstep.member.domain;

public enum MemberType {
    ADULT,
    TEENAGER,
    CHILDREN;

    MemberType() {
    }

    public static MemberType getMemberType(Integer age) {
        if (age == null) {
            return null;
        }

        if (age >= 6 && age <= 12) {
            return CHILDREN;
        } else if (age >= 13 && age <= 18) {
            return TEENAGER;
        } else {
            return ADULT;
        }
    }
}
