package nextstep.member.domain;

import org.apache.logging.log4j.util.Strings;

public class NoLoginMember {

    public static final LoginMember MEMBER = new LoginMember(null, Strings.EMPTY, Strings.EMPTY, null);

}
