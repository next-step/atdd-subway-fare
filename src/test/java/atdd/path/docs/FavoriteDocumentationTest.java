package atdd.path.docs;

import atdd.AbstractDocumentationTest;
import atdd.path.application.FavoritePathService;
import atdd.path.application.FavoriteStationService;
import atdd.path.web.FavoriteController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static atdd.TestConstant.FAVORITE_STATION_RESPONSE;
import static atdd.TestConstant.TEST_USER_TOKEN;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteController.class)
public class FavoriteDocumentationTest extends AbstractDocumentationTest {
    public static final String HEADER_NAME = "Authorization";

    @MockBean
    private FavoriteStationService favoriteStationService;

    @MockBean
    private FavoritePathService favoritePathService;

    @Test
    void addFavoriteStation() throws Exception {
        given(favoriteStationService.addFavoriteStation(anyString(), anyLong())).willReturn(FAVORITE_STATION_RESPONSE);

        //when
        ResultActions result = this.mockMvc.perform(post("/favorites/stations")
                .header(HEADER_NAME, "Bearer " + TEST_USER_TOKEN)
                .content(String.valueOf(1))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isCreated())
                .andDo(
                        document("favorites/stations",
                                requestHeaders(
                                        headerWithName(HEADER_NAME).description("Bearer auth credentials")
                                ),
//                                requestFields(
//                                        fieldWithPath("stationId").type(JsonFieldType.NUMBER).description("The station's ID")
//                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description(" The favorite station id"),
                                        fieldWithPath("owner").type(JsonFieldType.NUMBER).description(" The favorite station's owner"),
                                        fieldWithPath("station.id").type(JsonFieldType.NUMBER).description(" The station's id"),
                                        fieldWithPath("station.name").type(JsonFieldType.STRING).description(" The station's name"),
                                        fieldWithPath("station.lines[].id").type(JsonFieldType.NUMBER).description(" The line's id"),
                                        fieldWithPath("station.lines[].name").type(JsonFieldType.STRING).description(" The line's name")
                                )
                        ))
                .andDo(print());
    }
}
