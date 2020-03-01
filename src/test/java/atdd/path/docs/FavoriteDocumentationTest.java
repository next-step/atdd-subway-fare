package atdd.path.docs;

import atdd.AbstractDocumentationTest;
import atdd.path.application.FavoriteService;
import atdd.path.application.dto.FavoriteResponseView;
import atdd.path.domain.Favorite;
import atdd.path.domain.Station;
import atdd.path.domain.User;
import atdd.path.web.FavoriteController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static atdd.TestConstant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteController.class)
public class FavoriteDocumentationTest extends AbstractDocumentationTest {
    private final Station STATION = new Station(1L, "강남역");
    private final User USER = new User(1L, TEST_USER_EMAIL, TEST_USER_NAME);
    private List<Station> stations = new ArrayList<Station>();
    private final Favorite FAVORITE = new Favorite(1L, USER,  stations);

    @BeforeEach
    void setUp() {
        stations.add(STATION);
    }

    @MockBean
    private FavoriteService favoriteService;

    @DisplayName("지하철역 즐겨찾기 등록")
    @Test
    public void createFavoriteForStation() throws Exception {
        String inputJson = String.format("{\"name\": \"%s\"}", "강남역");

       given(favoriteService.addStationFavorite(any(), any())).willReturn(ResponseEntity.of(Optional.of(FAVORITE)));

        mockMvc.perform(post("/favorites/stations").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        document("favorites/stations/create",
                                requestFields(fieldWithPath("name").type(JsonFieldType.STRING).description("The station name"))
                        )
                )
                .andDo(print());
    }

    @DisplayName("지하철역 즐겨찾기 받기")
    @Test
    public void deleteFavoriteForStation() throws Exception {
        given(favoriteService.retriveStationFavorites(any())).willReturn(ResponseEntity.of(Optional.of(FAVORITE).map(FavoriteResponseView::of)));

        mockMvc.perform(get("/favorites/stations").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andDo(
                        document("favorites/stations/read",
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The favorite id"),
                                        fieldWithPath("stations[].id").type(JsonFieldType.NUMBER)
                                                .description("station id in favorite stations"),
                                        fieldWithPath("stations[].name").type(JsonFieldType.STRING)
                                                .description("station name in favoirte stations"),
                                        fieldWithPath("stations[].lines").type(JsonFieldType.ARRAY)
                                                .description("lines to which favoirte station is belonged").optional()
                                )
                        )
                )
                .andDo(print());
    }
}
