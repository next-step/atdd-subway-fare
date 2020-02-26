package atdd.docs;

import atdd.Constant;
import atdd.path.application.LineService;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.transaction.annotation.Transactional;

import static atdd.path.TestConstant.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class PathDocumentationTest extends BaseDocumentationTest {
    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Line line;

    @Autowired
    StationDao stationDao;

    @Autowired
    LineDao lineDao;

    @Autowired
    LineService lineService;

    @Test
    public void 최소_경로_조회하기() throws Exception {
        //given
        setUpForPathTest();

        //when, then
        mockMvc.perform(
                get(Constant.PATH_BASE_URI)
                        .param("startId", station2.getId().toString())
                        .param("endId", station5.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("find-path",
                        links(halLinks(),
                                linkWithRel("self")
                                        .description("link to self"),
                                linkWithRel("favorite-path-create")
                                        .description("link to create a favorite-path"),
                                linkWithRel("profile")
                                        .description("link to describe it by itself")
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
                                        .description("The id of the station to start"),
                                fieldWithPath("endStationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The id of the station to arrive"),
                                fieldWithPath("stations")
                                        .type(JsonFieldType.ARRAY)
                                        .description("It shows the list of the station in the path"),
                                fieldWithPath("stations[0].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("The station Id"),
                                fieldWithPath("stations[0].name")
                                        .type(JsonFieldType.STRING)
                                        .description("The station name"),
                                fieldWithPath("stations[0].lines")
                                        .type(JsonFieldType.NULL)
                                        .description("The line list of a station"),
                                fieldWithPath("_links.self.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to self"),
                                fieldWithPath("_links.favorite-path-create.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to create a favorite-path"),
                                fieldWithPath("_links.profile.href")
                                        .type(JsonFieldType.STRING)
                                        .description("link to describe it by itself")
                        )
                ));
    }

    public void setUpForPathTest() {
        station1 = stationDao.save(new Station(STATION_NAME_23));
        station2 = stationDao.save(new Station(STATION_NAME_24));
        station3 = stationDao.save(new Station(STATION_NAME_25));
        station4 = stationDao.save(new Station(STATION_NAME_26));
        station5 = stationDao.save(new Station(STATION_NAME_27));
        line = lineDao.save(Line.of(LINE_NAME_3, START_TIME, END_TIME, INTERVAL_MIN));
        lineService.addEdge(line.getId(), station1.getId(), station2.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station2.getId(), station3.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station3.getId(), station4.getId(), DISTANCE_KM);
        lineService.addEdge(line.getId(), station4.getId(), station5.getId(), DISTANCE_KM);
    }
}