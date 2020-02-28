package atdd.member.application.dto;

import atdd.member.domain.Member;

public class MemberResponseView {
    private Long id;
    private String email;
    private String password;
    private String name;

    public MemberResponseView() {
    }

    public MemberResponseView(Member persistMember) {
        this.id = persistMember.getId();
        this.email = persistMember.getEmail();
        this.password = persistMember.getPassword();
        this.name = persistMember.getName();
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
