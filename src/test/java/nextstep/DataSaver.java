package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSaver {
    public static final String MEMBER_CHILD_EMAIL = "child_email";
    public static final String MEMBER_CHILD_PASSWORD = "child_password";
    public static final Integer MEMBER_CHILD_AGE = 12;

    public static final String MEMBER_TEENAGER_EMAIL = "teenager_email";
    public static final String MEMBER_TEENAGER_PASSWORD = "teenager_password";
    public static final Integer MEMBER_TEENAGER_AGE = 18;

    public static final String MEMBER_ADULT_EMAIL = "adult_email";
    public static final String MEMBER_ADULT_PASSWORD = "adult_password";
    public static final Integer MEMBER_ADULT_AGE = 19;

    @Autowired
    private MemberRepository memberRepository;

    public void saveChildMember() {
        memberRepository.save(new Member(MEMBER_CHILD_EMAIL, MEMBER_CHILD_PASSWORD, MEMBER_CHILD_AGE));
    }

    public void saveTeenagerMember() {
        memberRepository.save(new Member(MEMBER_TEENAGER_EMAIL, MEMBER_TEENAGER_PASSWORD, MEMBER_TEENAGER_AGE));
    }

    public void saveAdultMember() {
        memberRepository.save(new Member(MEMBER_ADULT_EMAIL, MEMBER_ADULT_PASSWORD, MEMBER_ADULT_AGE));
    }
}
