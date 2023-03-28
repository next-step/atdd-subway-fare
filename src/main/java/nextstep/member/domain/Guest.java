package nextstep.member.domain;

import java.util.List;

import nextstep.member.exception.MemberErrorMessage;
import nextstep.member.ui.AuthenticationException;

public class Guest extends LoginMember {

	private Guest() {
		super();
	}

	public static Guest getInstance() {
		return SingletonHelper.GUEST;
	}

	@Override
	public Long getId() {
		throw new AuthenticationException(MemberErrorMessage.UNAUTHENTICATION_USER);
	}

	@Override
	public List<String> getRoles() {
		throw new AuthenticationException(MemberErrorMessage.UNAUTHENTICATION_USER);
	}

	private static class SingletonHelper {
		private static final Guest GUEST = new Guest();
	}
}
