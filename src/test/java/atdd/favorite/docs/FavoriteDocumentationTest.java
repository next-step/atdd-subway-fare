package atdd.favorite.docs;

import atdd.AbstractDocumentationTest;
import atdd.favorite.application.FavoriteService;
import atdd.favorite.web.FavoriteController;
import atdd.path.application.GraphService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;
import java.util.Map;

import static atdd.TestConstant.*;
import static atdd.TestUtils.jsonOf;
import static atdd.favorite.web.FavoriteController.FAVORITES_STATIONS_URL;
import static atdd.security.JwtTokenProvider.TOKEN_TYPE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FavoriteController.class)
public class FavoriteDocumentationTest extends AbstractDocumentationTest {

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private GraphService graphService;

    @DisplayName("지하철역 즐겨찾기 등록을 할 수 있다")
    @Test
    void beAbleToSaveForStation() throws Exception {
        given(favoriteService.saveForStation(TEST_MEMBER, STATION_ID)).willReturn(TEST_FAVORITE_STATION);

        mockMvc.perform(post(FAVORITES_STATIONS_URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + TEST_MEMBER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(Map.of("stationId", STATION_ID))))
                .andDo(
                        document(FAVORITES_STATIONS_URL + "/create",
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("The bearer token"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                                ),
                                requestFields(
                                        fieldWithPath("stationId").description("The station's id")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The favorite's id"),
                                        fieldWithPath("station.id").type(JsonFieldType.NUMBER).description("The station's id"),
                                        fieldWithPath("station.name").type(JsonFieldType.STRING).description("The station's name"),
                                        fieldWithPath("station.lines").type(JsonFieldType.ARRAY).description("The station's lines")
                                )))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("지하철역 즐겨찾기 목록을 조회 할 수 있다")
    @Test
    void beAbleToFindForStation() throws Exception {
        given(favoriteService.findForStations(TEST_MEMBER)).willReturn(List.of(TEST_FAVORITE_STATION));

        mockMvc.perform(get(FAVORITES_STATIONS_URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + TEST_MEMBER_TOKEN))
                .andDo(
                        document(FAVORITES_STATIONS_URL + "/find",
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("The bearer token")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                                ),
                                responseFields(
                                        fieldWithPath("count").type(JsonFieldType.NUMBER).description("The favorite station's count"),
                                        fieldWithPath("favorites[].id").type(JsonFieldType.NUMBER).description("The favorite's id"),
                                        fieldWithPath("favorites[].station.id").type(JsonFieldType.NUMBER).description("The station's id"),
                                        fieldWithPath("favorites[].station.name").type(JsonFieldType.STRING).description("The station's name"),
                                        fieldWithPath("favorites[].station.lines").type(JsonFieldType.ARRAY).description("The station's lines")
                                )))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("지하철역 즐겨찾기 삭제를 할 수 있다")
    @Test
    void beAbleToDeleteForStation() throws Exception {
        doNothing().when(favoriteService).deleteForPathById(FAVORITE_STATION_ID);

        mockMvc.perform(delete(FAVORITES_STATIONS_URL + "/{id}", FAVORITE_STATION_ID)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + TEST_MEMBER_TOKEN))
                .andDo(
                        document(FAVORITES_STATIONS_URL + "/delete",
                                pathParameters(
                                        parameterWithName("id").description("The favorite's id")
                                )))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}
