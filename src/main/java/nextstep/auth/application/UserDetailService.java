package nextstep.auth.application;

public interface UserDetailService {
    UserDetails findByEmail(String email);
    UserDetails findMemberOrCreate(String email, Integer age);
}
