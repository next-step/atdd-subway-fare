package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.UserDetailService;
import nextstep.auth.domain.LoginMember;
import nextstep.auth.domain.UserDetails;
import nextstep.common.exception.MemberNotFoundException;
import nextstep.member.domain.Member;
import nextstep.member.domain.UserDetailsMapper;
import nextstep.member.infrastructure.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailService {
    private final MemberRepository memberRepository;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public UserDetails findByEmail(String email) {
        var member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException(email));
        return userDetailsMapper.toUserDetails(member);
    }

    @Override
    public UserDetails findOrElseDefault(String email, Supplier<UserDetails> defaultSupplier) {
        return memberRepository.findByEmail(email)
                .map(userDetailsMapper::toUserDetails)
                .orElseGet(() -> {
                    UserDetails userDetails = defaultSupplier.get();
                    Member newMember = new Member(userDetails.getPrincipal());
                    memberRepository.save(newMember);
                    return userDetails;
                });

    }

    @Override
    public int getUserAge(LoginMember loginMember) {
        var member = memberRepository.findByEmail(loginMember.getEmail()).orElse((null));
        return member == null ? 20 : member.getAge();
    }
}
