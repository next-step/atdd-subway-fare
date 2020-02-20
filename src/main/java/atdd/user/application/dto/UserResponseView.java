package atdd.user.application.dto;

import atdd.user.domain.User;

public class UserResponseView {
    private Long id;
    private String email;
    private String password;
    private String name;

    public UserResponseView() {
    }

    public UserResponseView(User persistUser) {
        this.id = persistUser.getId();
        this.email = persistUser.getEmail();
        this.password = persistUser.getPassword();
        this.name = persistUser.getName();
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
}
