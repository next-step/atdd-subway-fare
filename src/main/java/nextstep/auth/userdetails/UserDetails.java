package nextstep.auth.userdetails;

public interface UserDetails {
    String getUsername();

    String getPassword();

    String getRole();

    int getAge();
}
