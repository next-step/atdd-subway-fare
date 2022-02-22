package nextstep.member.domain;

public class NullLoginMember extends LoginMember{

    public NullLoginMember() {
        this(null, null, null, 0);
    }
     
    public NullLoginMember(Long id, String email, String password, Integer age) {
        super(id, email, password, age);
    }

    @Override
    public Integer getAge() {
        return 0;
    }
}
