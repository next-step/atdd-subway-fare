package atdd.path.docs;

import atdd.BaseDocumentationTest;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TimeTableDocumentationTest extends BaseDocumentationTest {
    private MockMvc mockMvc;
    private Station station = new Station(1L, "강남");
    private Station station2 = new Station(2L, "양재");
    private Line line
            = new Line(1L, "신분당선",
            LocalTime.of(05, 00), LocalTime.of(23, 00), 10);

    @Autowired
    public TimeTableDocumentationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void 문서화_지하철역_시간표_조회하기() throws Exception {
        String inputJson = "{\"name\":\"" + station.getName() + "\"}";
        mockMvc.perform(post("/stations/"+station.getId()+"/timetables")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("station-timetables",
                        links(halLinks(),
                                linkWithRel("self")
                                        .description("link to self")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT)
                                        .description("It accepts MediaType.APPLICATION_JSON"),
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("Its contentType is MediaType.APPLICATION_JSON")
                        ),
                        requestFields(

                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE)
                                        .description("The contentType is MediaType.APPLICATION_JSON")
                        ),
                        responseFields(
                                fieldWithPath("lineId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the line where the station belongs"),
                                fieldWithPath("lineName")
                                        .type(JsonFieldType.STRING)
                                        .description("The name of the line where the station belongs"),
                                fieldWithPath("timeTables")
                                        .type(JsonFieldType.ARRAY)
                                        .description("The list of timetables of the subway at the station"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self")
                        )
                ));
    }
}
