package support.auth.userdetails;

import java.util.Collections;
import java.util.List;

public class AnonymousUser implements UserDetails {
    @Override
    public Object getUsername() {
        return "";
    }

    @Override
    public Object getPassword() {
        return "";
    }

    @Override
    public List<String> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return false;
    }
}
