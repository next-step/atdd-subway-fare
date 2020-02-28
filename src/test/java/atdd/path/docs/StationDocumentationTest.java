package atdd.path.docs;

import atdd.AbstractDocumentationTest;
import atdd.path.domain.Station;
import atdd.path.repository.StationRepository;
import atdd.path.web.StationController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

        this.mockMvc
                .perform(post("/stations").content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("stations/create",
                        requestFields(fieldWithPath("name").type(JsonFieldType.STRING).description("The station name")),
                        this.stationResponseFields()
                    )
                )
                .andDo(print());
    }

    @DisplayName("지하철역 목록을 받아온다.")
    @Test
    public void retrieveStations() throws Exception {
        Station gangnam = new Station(1L, "강남역");
        Station yuksam = new Station(2L, "역삼역");
        Station seongreung = new Station(3L, "선릉역");

        List<Station> stations = new ArrayList<Station>();
        stations.add(gangnam);
        stations.add(yuksam);
        stations.add(seongreung);

        given(stationRepository.findAll()).willReturn(stations);

        FieldDescriptor[] station = new FieldDescriptor[]{
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("The station id"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The station name"),
                fieldWithPath("lines").type(JsonFieldType.ARRAY).description("lines to which the station belong").optional()};

        this.mockMvc
                .perform(get("/stations").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("stations/read",
                        responseFields(fieldWithPath("[]").description("An array of stations")
                        ).andWithPrefix("[].", station)))
                .andDo(print());
    }

    @DisplayName("특정 지하철역을 받아온다.")
    @Test
    public void retrieveStation() throws Exception {
        Station gangnam = new Station(1L, "강남역");

        given(stationRepository.findById(gangnam.getId())).willReturn(Optional.of(gangnam));

        this.mockMvc
                .perform(get("/stations"+"/" + gangnam.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("stations/read/{id}", this.stationResponseFields()))
                .andDo(print());
    }

    @DisplayName("특정 지하철역을 삭제한다.")
    @Test
    public void deleteStation() throws Exception {
        Station gangnam = new Station(1L, "강남역");

        this.mockMvc
                .perform(delete("/stations"+"/" + gangnam.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(document("stations/delete/{id}"))
                .andDo(print());
    }

    private ResponseFieldsSnippet stationResponseFields() {
        return responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("The station id"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("The station name"),
                        fieldWithPath("lines").type(JsonFieldType.ARRAY).description("lines to which the station belong").optional()
                );
    }
}
