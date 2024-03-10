package nextstep.core.member.fixture;

public enum MemberFixture {
    스미스("smith@email.com", "smith_password", 5),
    존슨("johnson@email.com", "johnson_password", 12),
    윌리엄스("williams@email.com", "williams_password", 13),
    브라운("brown@email.com", "brown_password", 18),
    잭슨("jackson@email.com", "jackson_password", 19);

    public final String 이메일;
    public final String 비밀번호;
    public final int 나이;

    MemberFixture(String 이메일, String 비밀번호, int 나이) {
        this.이메일 = 이메일;
        this.비밀번호 = 비밀번호;
        this.나이 = 나이;
    }

    public String get이메일() {
        return 이메일;
    }

    public String get비밀번호() {
        return 비밀번호;
    }

    public int get나이() {
        return 나이;
    }
}
