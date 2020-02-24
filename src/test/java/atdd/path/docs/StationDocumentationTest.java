package atdd.path.docs;

import atdd.AbstractDocumentationTest;
import atdd.path.domain.Station;
import atdd.path.repository.StationRepository;
import atdd.path.web.StationController;
import net.bytebuddy.build.ToStringPlugin;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(StationController.class)
public class StationDocumentationTest extends AbstractDocumentationTest {

    @MockBean
    private StationRepository stationRepository;

    @Test
    public void create() throws Exception {
        Station station = new Station(1L, "강남역");

        String inputJson = "{\"name\":\"" + station.getName() + "\"}";

        given(stationRepository.save(any())).willReturn(station);

        this.mockMvc.perform(post("/stations").content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(
                        document("stations/create",
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("The station name")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The station id"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("The station name"),
                                        fieldWithPath("lines").type(JsonFieldType.ARRAY).description("lines to which the station belong")
                                )
                        )
                )
                .andDo(print());
    }

    @Test
    public void retrieveStations() {
        Station gangnam = new Station(1L, "강남역");
        Station yuksam = new Station(2L, "역삼역");
        Station seongreung = new Station(3L, "선릉역");

        List<Station> stations = new ArrayList<Station>();
        stations.add(gangnam);
        stations.add(yuksam);
        stations.add(seongreung);

        given(stationRepository.findAll()).willReturn(stations);
    }

}
