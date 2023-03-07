package nextstep.subway.unit;

import nextstep.member.domain.AnonymousUser;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.FareService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import nextstep.subway.utils.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class FareServiceTest {

    @Autowired
    private FareService fareService;
    @Autowired
    private MemberRepository memberRepository;

    private AnonymousUser 익명사용자;
    private LoginMember 어린이사용자;
    private LoginMember 청소년사용자;
    private LoginMember 성인사용자;

    private Path path;

    @BeforeEach
    void setUp() {
        // 사용자
        Member 어린이 = memberRepository.findByEmail(Users.어린이.getEmail()).get();
        Member 청소년 = memberRepository.findByEmail(Users.청소년.getEmail()).get();
        Member 성인 = memberRepository.findByEmail(Users.성인.getEmail()).get();

        어린이사용자 = new LoginMember(어린이.getId(), 어린이.getRoles());
        청소년사용자 = new LoginMember(청소년.getId(), 청소년.getRoles());
        성인사용자 = new LoginMember(성인.getId(), 성인.getRoles());
        익명사용자 = new AnonymousUser();

        // path
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 선릉역 = new Station("선릉역");
        Station 삼성역 = new Station("삼성역");

        Line 이호선 = new Line("2호선", "green", 0);
        Line 삼호선 = new Line("3호선", "orange", 500);
        Line 신분당선 = new Line("신분당선", "red", 900);

        Sections sections = new Sections(
                List.of(
                        new Section(이호선, 강남역, 역삼역, 10, 10),
                        new Section(삼호선, 역삼역, 선릉역, 2, 2),
                        new Section(신분당선, 선릉역, 삼성역, 4, 4)
                )
        );
        path = new Path(sections);
    }

    @Test
    void getTotalFareOf어린이사용자() {
        int totalFare = fareService.getTotalFare(어린이사용자, path);

        assertThat(totalFare).isEqualTo((int) ((1_250 + 200 + 900) * 0.5));
    }

    @Test
    void getTotalFareOf청소년사용자() {
        int totalFare = fareService.getTotalFare(청소년사용자, path);

        assertThat(totalFare).isEqualTo((int) ((1_250 + 200 + 900) * 0.8));
    }

    @Test
    void getTotalFareOf성인사용자() {
        int totalFare = fareService.getTotalFare(성인사용자, path);

        assertThat(totalFare).isEqualTo(1_250 + 200 + 900);
    }

    @Test
    void getTotalFareOf익명사용자() {
        int totalFare = fareService.getTotalFare(익명사용자, path);

        assertThat(totalFare).isEqualTo(1_250 + 200 + 900);
    }
}
