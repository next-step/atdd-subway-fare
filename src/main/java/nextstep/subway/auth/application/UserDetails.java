package nextstep.subway.auth.application;

public interface UserDetails {

    Object getUsername();
    Object getPassword();

    boolean validatePassword(Object password);
}
