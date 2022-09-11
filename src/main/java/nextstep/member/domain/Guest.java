package support.auth.userdetails;

import java.util.List;

public class AnonymousUser implements UserDetails {

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public List<String> getAuthorities() {
        return null;
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return false;
    }
}
