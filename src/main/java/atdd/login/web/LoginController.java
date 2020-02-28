package atdd.login.web;

import atdd.login.application.dto.LoginRequestView;
import atdd.login.application.dto.LoginResponseView;
import atdd.member.dao.MemberDao;
import atdd.member.domain.Member;
import atdd.security.JwtTokenProvider;
import atdd.security.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static atdd.security.JwtTokenProvider.TOKEN_TYPE;

@RequestMapping("/login")
@RestController
public class LoginController {

    public static final String LOGIN_URL = "/login";

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<LoginResponseView> login(@RequestBody LoginRequestView view) {
        final String email = view.getEmail();
        final Member findMember = memberDao.findByEmail(email).orElseThrow(UnauthorizedException::new);

        if (!findMember.isMatchPassword(view.getPassword())) {
            throw new UnauthorizedException();
        }

        final String accessToken = jwtTokenProvider.createToken(email);
        return ResponseEntity.ok(new LoginResponseView(TOKEN_TYPE, accessToken));
    }

}
