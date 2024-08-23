package nextstep.member.domain;

public class AnonymousMember extends Member {
    public AnonymousMember() {
        super();
    }

    public AnonymousMember(String email, String password, Integer age) {
        super(email, password, age);
    }

    public AnonymousMember(Long id, String email, String password, Integer age) {
        super(id, email, password, age);
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public Integer getAge() {
        return super.getAge();
    }

    @Override
    public void update(Member member) {
        super.update(member);
    }

    @Override
    public boolean checkPassword(String password) {
        return super.checkPassword(password);
    }
}

