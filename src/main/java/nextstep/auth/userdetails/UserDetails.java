package nextstep.auth.userdetails;

public interface UserDetails {
    String getUsername();

    String getPassword();

    Integer getAge();

    String getRole();
}
