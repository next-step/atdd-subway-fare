package atdd.user.domain;

import atdd.exception.ErrorType;
import atdd.exception.SubwayException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.mindrot.jbcrypt.BCrypt;

public class User {
    private Long id;
    private String email;
    @JsonIgnore
    private String password;
    private String name;

    public User() {
    }

    public User(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void encryptPassword() {
        this.password = encrypt(this.password);
    }

    public void checkPassword(String password) {
        if (BCrypt.checkpw(password, encrypt(this.password)))
            throw new SubwayException(ErrorType.INVALID_PASSWORD);
    }

    private String encrypt(final String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
