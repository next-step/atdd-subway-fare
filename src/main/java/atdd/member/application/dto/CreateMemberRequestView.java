package atdd.member.application.dto;

import atdd.member.domain.Member;

public class CreateMemberRequestView {
    private Long id;
    private String email;
    private String password;
    private String name;

    public CreateMemberRequestView() {
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Member toMember() {
        return new Member(id, email, name, password);
    }
}
