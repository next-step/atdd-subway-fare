package support.auth.userdetails;

import java.util.List;

public interface UserDetails {
    Object getUsername();

    Object getPassword();

    int getAge();

    List<String> getAuthorities();

    boolean checkCredentials(Object credentials);
}
