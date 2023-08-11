package nextstep.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    default Member findMemberByEmail(String email) {
        return findByEmail(email).orElseThrow(RuntimeException::new);
    }

}
