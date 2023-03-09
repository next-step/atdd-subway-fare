package nextstep.subway.utils;

public enum Users {
    성인("adult@email.com", "password", 38),
    청소년("teenager@email.com", "password", 17),
    어린이("children@email.com", "password", 7),
    ;

    private final String email;
    private final String password;
    private final int age;

    Users(String email, String password, int age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

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
