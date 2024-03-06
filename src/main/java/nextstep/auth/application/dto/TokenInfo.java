package nextstep.auth.application.dto;

import java.util.Objects;

public class TokenInfo {
    private final Long id;
    private final String email;
    private final Integer age;

    public TokenInfo(final Long id, final String email, final Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final TokenInfo tokenInfo = (TokenInfo) object;
        return Objects.equals(id, tokenInfo.id) && Objects.equals(email, tokenInfo.email) && Objects.equals(age, tokenInfo.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, age);
    }
}
