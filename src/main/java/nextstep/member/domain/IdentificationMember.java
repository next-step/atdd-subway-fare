package nextstep.member.domain;

import java.util.List;

public interface IdentificationMember {

    Long getId();

    List<String> getRoles();

    boolean isAnonymousMember();
}
