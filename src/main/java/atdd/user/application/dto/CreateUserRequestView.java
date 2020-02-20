package atdd.user.application.dto;

import atdd.user.domain.User;

public class CreateUserRequestView {
    private Long id;
    private String email;
    private String password;
    private String name;

    public CreateUserRequestView(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User toUser() {
        return new User(id, email, password, name);
    }
}
