package atdd.user.application.dto;

import atdd.user.domain.User;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserResponseView {
    private Long id;
    private String email;
    private String password;
    private String name;

    @Builder
    public UserResponseView(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
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


    public static UserResponseView of(User user) {
        return UserResponseView.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .build();
    }
}
