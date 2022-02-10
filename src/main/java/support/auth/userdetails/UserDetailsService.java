package support.auth.userdetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String principal);
}
