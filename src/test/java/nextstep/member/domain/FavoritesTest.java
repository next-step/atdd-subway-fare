package nextstep.member.domain;

import nextstep.member.domain.exception.DuplicateFavoriteException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoritesTest {

    @Test
    void add() {
        Favorites favorites = new Favorites();

        favorites.add(new Favorite(1L, 2L));

        assertThat(favorites.getFavorites()).hasSize(1);
    }

    @Test
    void addDuplicate() {
        Favorites favorites = new Favorites();
        favorites.add(new Favorite(1L, 2L));

        assertThatThrownBy(() -> favorites.add(new Favorite(1L, 2L)))
                .isInstanceOf(DuplicateFavoriteException.class)
                .hasMessage(DuplicateFavoriteException.DEFAULT_MESSAGE);
    }

}
