package nextstep.auth.userdetails;

public interface UserDetails {
    Object getPrincipal();

    Object getCredentials();

    boolean checkCredentials(Object credentials);
}
