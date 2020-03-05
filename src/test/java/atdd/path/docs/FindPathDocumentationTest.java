package atdd.path.docs;

import atdd.BaseDocumentationTest;
import atdd.path.application.GraphService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindPathDocumentationTest extends BaseDocumentationTest {
    private MockMvc mockMvc;

    @Autowired
    public FindPathDocumentationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @MockBean
    GraphService graphService;

    @Test
    void 문서화_최소_경로_조회하기() throws Exception {
        //given
        given(graphService.findPath(anyLong(), anyLong()))
                .willReturn(Arrays.asList(STATION_1, STATION_2, STATION_3));

        //when, then
        mockMvc.perform(get("/paths")
                .param("startId", stationId1.toString())
                .param("endId", stationId3.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("find-path",
                        links(halLinks(),
                                linkWithRel("self")
                                        .description("link to self"),
                                linkWithRel("profile")
                                        .description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT)
                                        .description("It accepts MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("Its contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("The contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("startStationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The stationId to start"),
                                fieldWithPath("endStationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The stationId to end"),
                                fieldWithPath("stations")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The list of the stations in the path"),
                                fieldWithPath("stations[0].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the station in the path"),
                                fieldWithPath("stations[0].name")
                                        .type(JsonFieldType.STRING)
                                        .description("The name of the station in the path"),
                                fieldWithPath("stations[0].lines")
                                        .type(JsonFieldType.NULL)
                                        .description("The list of the lines where the station belongs"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to profile")
                        )
                ));
    }


}
