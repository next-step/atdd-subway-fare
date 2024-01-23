package nextstep.favorite.domain;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class FavoriteRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void save() {
        // given
        Station 강남역 = new Station("강남역");
        entityManager.persist(강남역);
        Station 양재역 = new Station("양재역");
        entityManager.persist(양재역);
        Member member = new Member("email@email.com", "password", 12);
        Favorite favorite = new Favorite(강남역, 양재역, member);

        // when
        Favorite savedFavorite = favoriteRepository.save(favorite);

        // then
        assertNotNull(savedFavorite.getId());
    }

    @Test
    void isCreatedBy() {
        // given
        Station 강남역 = new Station("강남역");
        entityManager.persist(강남역);
        Station 양재역 = new Station("양재역");
        entityManager.persist(양재역);
        Member 회원 = new Member("email", "password", 12);
        entityManager.persist(회원);
        Favorite savedFavorite = favoriteRepository.save(new Favorite(강남역, 양재역, 회원));

        entityManager.clear();
        entityManager.flush();

        // when
        Favorite favorite = favoriteRepository.findById(savedFavorite.getId()).orElseThrow(RuntimeException::new);
        Member member = memberRepository.findById(savedFavorite.getMember().getId()).orElseThrow(RuntimeException::new);

        // then
        assertThat(favorite.isCreatedBy(member)).isTrue();
    }
}