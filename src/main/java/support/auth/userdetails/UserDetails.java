package support.auth.userdetails;

import java.util.List;

public interface UserDetails {
    Object getUsername();

    Object getPassword();

    List<String> getAuthorities();

    Integer getAge();

    boolean checkCredentials(Object credentials);
}
