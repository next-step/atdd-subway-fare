package nextstep.auth.userdetails;

public interface UserDetails {
    Object getPrincipal();

    Object getCredentials();

    Integer getAge();

    boolean checkCredentials(Object credentials);
}
