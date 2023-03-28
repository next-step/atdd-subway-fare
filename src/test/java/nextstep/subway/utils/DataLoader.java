package nextstep.subway.utils;

import java.util.Arrays;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;

@Profile("test")
@Component
public class DataLoader {
	private MemberRepository memberRepository;

	public DataLoader(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public void loadData() {
		memberRepository.save(new Member("admin@email.com", "password", 20, Arrays.asList(RoleType.ROLE_ADMIN.name())));
		memberRepository.save(
			new Member("member@email.com", "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
		memberRepository.save(
			new Member("teenager@email.com", "password", 13, Arrays.asList(RoleType.ROLE_MEMBER.name())));
		memberRepository.save(
			new Member("children@email.com", "password", 6, Arrays.asList(RoleType.ROLE_MEMBER.name())));
		memberRepository.save(
			new Member(GithubResponses.사용자1.getEmail(), "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
		memberRepository.save(
			new Member(GithubResponses.사용자2.getEmail(), "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
		memberRepository.save(
			new Member(GithubResponses.사용자3.getEmail(), "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
		memberRepository.save(
			new Member(GithubResponses.사용자4.getEmail(), "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
	}
}
