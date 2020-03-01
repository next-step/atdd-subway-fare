package atdd.path.docs;

import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LineControllerTest {
    public MockMvc mockMvc;

    public LineControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public ResultActions createTest(String uri, String inputJson) throws Exception {
        return mockMvc.perform(post(uri).content(inputJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    public ResultActions createTest(String uri, Long id, String inputJson) throws Exception {
        return mockMvc
                .perform(
                        RestDocumentationRequestBuilders
                                .post(uri, id)
                                .content(inputJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    public ResultActions retrieveAllTest(String uri) throws Exception {
        return mockMvc.perform(get(uri).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    public ResultActions retrieveTest(String uri, Long id) throws Exception {
        return mockMvc.perform(
                        RestDocumentationRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    public ResultActions deleteTest(String uri, Long id) throws Exception {
        return mockMvc.perform(RestDocumentationRequestBuilders.delete(uri, id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    public ResultActions deleteTest(String uri, Long lineId, Long stationId) throws Exception {
        return mockMvc
                .perform(
                        RestDocumentationRequestBuilders.delete(uri, lineId, stationId)
                )
                .andExpect(status().isOk());
    }

}
