package nextstep.subway.auth.userdetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String principal);
    UserDetails getEmptyUser();
}
