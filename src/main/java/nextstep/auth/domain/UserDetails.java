package nextstep.auth.domain;

public interface UserDetails {
    String getUsername();
    String getPassword();
    Integer getAge();
}
