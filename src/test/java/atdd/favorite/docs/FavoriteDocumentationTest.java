package atdd.favorite.docs;

import atdd.AbstractDocumentationTest;
import atdd.favorite.application.FavoriteService;
import atdd.favorite.web.FavoriteController;
import atdd.path.application.GraphService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;
import java.util.Map;

import static atdd.TestConstant.*;
import static atdd.TestUtils.jsonOf;
import static atdd.favorite.web.FavoriteController.FAVORITES_PATH_URL;
import static atdd.favorite.web.FavoriteController.FAVORITES_STATIONS_URL;
import static atdd.security.JwtTokenProvider.TOKEN_TYPE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
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

    @Test
    void beAbleToSaveForStation() throws Exception {
        given(favoriteService.saveForStation(TEST_MEMBER, STATION_ID)).willReturn(TEST_FAVORITE_STATION);

        mockMvc.perform(post(FAVORITES_STATIONS_URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + TEST_MEMBER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(Map.of("stationId", STATION_ID))))
                .andDo(
                        document(FAVORITES_STATIONS_URL + "/create",
                                requestHeaders(getHeaderAuthorizationAndContentType()),
                                requestFields(
                                        fieldWithPath("stationId").description("The station's id")
                                ),
                                responseHeaders(getHeaderLocationAndContentType()),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The favorite station's id"))
                                        .andWithPrefix("station.", getDescriptionForStation())

                        ))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void beAbleToFindForStation() throws Exception {
        given(favoriteService.findForStations(TEST_MEMBER)).willReturn(List.of(TEST_FAVORITE_STATION));

        mockMvc.perform(get(FAVORITES_STATIONS_URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + TEST_MEMBER_TOKEN))
                .andDo(
                        document(FAVORITES_STATIONS_URL + "/find",
                                requestHeaders(getHeaderAuthorization()),
                                responseHeaders(getHeaderContentType()),
                                responseFields(
                                        fieldWithPath("count").type(JsonFieldType.NUMBER).description("The favorite station's count"),
                                        fieldWithPath("favorites[].id").type(JsonFieldType.NUMBER).description("The favorite station's id"))
                                        .andWithPrefix("favorites[].station.", getDescriptionForStation())
                        ))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void beAbleToDeleteForStation() throws Exception {
        doNothing().when(favoriteService).deleteForPathById(FAVORITE_STATION_ID);

        mockMvc.perform(delete(FAVORITES_STATIONS_URL + "/{id}", FAVORITE_STATION_ID)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + TEST_MEMBER_TOKEN))
                .andDo(
                        document(FAVORITES_STATIONS_URL + "/delete",
                                requestHeaders(getHeaderAuthorization()),
                                pathParameters(parameterWithName("id").description("The favorite station's id"))
                        ))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void beAbleToSaveForPath() throws Exception {
        given(favoriteService.saveForPath(TEST_MEMBER, STATION_ID, STATION_ID_4)).willReturn(TEST_FAVORITE_PATH);
        given(graphService.findPath(TEST_FAVORITE_PATH)).willReturn(List.of(TEST_STATION, TEST_STATION_2, TEST_STATION_3, TEST_STATION_4));

        mockMvc.perform(post(FAVORITES_PATH_URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + TEST_MEMBER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(Map.of("startId", STATION_ID, "endId", STATION_ID_4))))
                .andDo(
                        document(FAVORITES_PATH_URL + "/create",
                                requestHeaders(getHeaderAuthorizationAndContentType()),
                                requestFields(
                                        fieldWithPath("startId").description("The start station's id"),
                                        fieldWithPath("endId").description("The end station's id")
                                ),
                                responseHeaders(getHeaderLocationAndContentType()),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The favorite path's id"))
                                        .andWithPrefix("path.", getDescriptionForPath())
                                        .andWithPrefix("path.stations[].", getDescriptionForStation())
                        ))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void beAbleToFindForPath() throws Exception {
        given(favoriteService.findForPaths(TEST_MEMBER)).willReturn(List.of(TEST_FAVORITE_PATH));
        given(graphService.findPath(TEST_FAVORITE_PATH)).willReturn(List.of(TEST_STATION, TEST_STATION_2, TEST_STATION_3, TEST_STATION_4));

        mockMvc.perform(get(FAVORITES_PATH_URL)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + TEST_MEMBER_TOKEN))
                .andDo(
                        document(FAVORITES_PATH_URL + "/find",
                                requestHeaders(getHeaderAuthorization()),
                                responseHeaders(getHeaderContentType()),
                                responseFields(
                                        fieldWithPath("count").type(JsonFieldType.NUMBER).description("The favorite path's count"),
                                        fieldWithPath("favorites[].id").type(JsonFieldType.NUMBER).description("The favorite path's id"))
                                        .andWithPrefix("favorites[].path.", getDescriptionForPath())
                                        .andWithPrefix("favorites[].path.stations[].", getDescriptionForStation())
                        ))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void beAbleToDeleteForPath() throws Exception {
        doNothing().when(favoriteService).deleteForPathById(FAVORITE_PATH_ID);

        mockMvc.perform(delete(FAVORITES_PATH_URL + "/{id}", FAVORITE_PATH_ID)
                .header(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + TEST_MEMBER_TOKEN))
                .andDo(
                        document(FAVORITES_PATH_URL + "/delete",
                                requestHeaders(getHeaderAuthorization()),
                                pathParameters(parameterWithName("id").description("The favorite path's id"))))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private List<FieldDescriptor> getDescriptionForPath() {
        return List.of(
                fieldWithPath("startStationId").type(JsonFieldType.NUMBER).description("The station's start id"),
                fieldWithPath("endStationId").type(JsonFieldType.NUMBER).description("The station's end id")
        );
    }

    private List<FieldDescriptor> getDescriptionForStation() {
        return List.of(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("The station's id"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The station's name"),
                fieldWithPath("lines").type(JsonFieldType.ARRAY).description("The station's lines").optional()
        );
    }

}
