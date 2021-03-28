package nextstep.subway.auth.domain;

import nextstep.subway.exceptions.NotFoundLoginTypeException;

import java.util.Arrays;

public enum UserType {
    ANONYMOUS,
    USER;

    UserType() {
    }

    public static UserType get(String str) {
        return Arrays.stream(UserType.values())
                .filter(type -> type.name().equals(str))
                .findFirst()
                .orElseThrow(NotFoundLoginTypeException::new);
    }
}
