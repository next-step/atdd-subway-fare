package nextstep.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class LoginMember {
    private final Long id;
    private final List<String> roles;
}
