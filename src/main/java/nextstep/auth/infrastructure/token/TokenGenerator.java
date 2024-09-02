package nextstep.auth.infrastructure.token;

public interface TokenGenerator {
    String createToken(String principal, Integer age);
    boolean validateToken(String token);
    String getPrincipal(String token);
}
