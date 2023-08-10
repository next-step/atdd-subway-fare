package nextstep.member.fixture;

public enum MemberAccounts {
    사용자1("yuseongan@next.com", "password", 23),
    청소년1("teenager@nextstep.com", "password", 17),
    어린이1("children@nextstep.com", "password", 6);

    MemberAccounts(String email, String password, int age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    private String email;
    private String password;
    private int age;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }
}
