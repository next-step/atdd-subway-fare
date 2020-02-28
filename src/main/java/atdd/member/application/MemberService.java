package atdd.member.application;

import atdd.member.dao.MemberDao;
import atdd.member.domain.Member;
import atdd.path.application.exception.NoDataException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Member findMemberByEmail(String email) {
        return memberDao.findByEmail(email).orElseThrow(NoDataException::new);
    }

    public Member save(Member member) {
        return memberDao.save(member);
    }

    public void deleteById(Long memberId) {
        memberDao.deleteById(memberId);
    }

}

