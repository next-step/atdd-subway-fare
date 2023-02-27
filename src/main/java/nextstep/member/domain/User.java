package nextstep.member.domain;

public interface User {
    Long getId();

    String getEmail();

    String getPassword();

    int getAge();

    boolean isGuest();

    int applyFarePolicy(int fare);
}
