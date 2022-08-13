package support.auth.userdetails;

import java.util.List;

public interface UserDetails {
    Object getUsername();

    Object getPassword();

    List<String> getAuthorities();

    int getAge();

    boolean checkCredentials(Object credentials);
}
