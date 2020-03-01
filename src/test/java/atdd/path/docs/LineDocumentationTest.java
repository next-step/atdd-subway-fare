package atdd.path.docs;

import atdd.AbstractDocumentationTest;
import atdd.path.application.LineService;
import atdd.path.domain.Line;
import atdd.path.domain.Station;
import atdd.path.repository.LineRepository;
import atdd.path.web.LineController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(LineController.class)
public class LineDocumentationTest extends AbstractDocumentationTest {
    private final RequestFieldsSnippet REQUEST_FIELD_SNIPPET = requestFields(
            fieldWithPath("name").type(JsonFieldType.STRING).description("The line name"),
            fieldWithPath("startTime").type(JsonFieldType.STRING).description("The line start time"),
            fieldWithPath("endTime").type(JsonFieldType.STRING).description("The line end time"),
            fieldWithPath("stationInterval").type(JsonFieldType.STRING).description("The interval between lines")
    );

    private final ResponseFieldsSnippet RESPONSE_FIELD_SNIPPET = responseFields(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("The line id"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("The line name"),
            fieldWithPath("stations").type(JsonFieldType.ARRAY).description("stations of this line").optional(),
            fieldWithPath("startTime").type(JsonFieldType.STRING).description("The line start time"),
            fieldWithPath("endTime").type(JsonFieldType.STRING).description("The line end time"),
            fieldWithPath("stationInterval").type(JsonFieldType.NUMBER).description("The interval between lines")
    );

    private final PathParametersSnippet PATH_PARAMETER_SNIPPET = pathParameters(
            parameterWithName("id").description("The line id")
    );



    private LineControllerTest lineControllerTest;

    @MockBean
    private LineRepository lineRepository;

    @MockBean
    private LineService lineService;

    @BeforeEach
    void setUp() {
        this.lineControllerTest = new LineControllerTest(mockMvc);
    }

    @DisplayName("지하철역 노선 생성")
    @Test
    public void createLine() throws Exception {
        Line line = new Line(1L, "2호선", LocalTime.of(5, 0), LocalTime.of(23, 30), 30);

        given(lineRepository.save(any())).willReturn(line);

        String inputJson = "{\"name\":\"" + line.getName() + "\"," +
                "\"startTime\":\"" + line.getStartTime() + "\"," +
                "\"endTime\":\"" + line.getEndTime() + "\"," +
                "\"stationInterval\":\"" + line.getStationInterval() + "\"}";

        ResultActions createResult = lineControllerTest.createTest("/lines", inputJson);

        RestDocumentationResultHandler documentation =
                createDocumentationResultHandler(
                        "lines/create",
                        REQUEST_FIELD_SNIPPET,
                        RESPONSE_FIELD_SNIPPET
                );

        createResult.andDo(documentation).andDo(print());
    }

    @DisplayName("지하철 노선 목록 받기")
    @Test
    public void retrieveLines() throws Exception {
        Line greenLine = new Line(1L, "2호선", LocalTime.of(5, 0), LocalTime.of(23, 30), 30);
        Line orangeLine = new Line(2L, "3호선", LocalTime.of(5, 0), LocalTime.of(23, 30), 30);
        Line blueLine = new Line(3L, "4호선", LocalTime.of(5, 0), LocalTime.of(23, 30), 30);

        List<Line> lines = new ArrayList<Line>();
        lines.add(greenLine);
        lines.add(orangeLine);
        lines.add(blueLine);

        given(lineRepository.findAll()).willReturn(lines);

        FieldDescriptor[] lineField = new FieldDescriptor[]{
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("The line id"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("The line name"),
                fieldWithPath("stations").type(JsonFieldType.ARRAY).description("stations of this line").optional(),
                fieldWithPath("startTime").type(JsonFieldType.STRING).description("The line start time"),
                fieldWithPath("endTime").type(JsonFieldType.STRING).description("The line end time"),
                fieldWithPath("stationInterval").type(JsonFieldType.NUMBER).description("The interval between lines")
        };

        ResponseFieldsSnippet retriveSnippet =
                responseFields(
                        fieldWithPath("[]").description("An array of line")
                ).andWithPrefix("[].", lineField);

        ResultActions retrieveAllResult = lineControllerTest.retrieveAllTest("/lines");;

        RestDocumentationResultHandler documentation = retrieveAllDocumentationResultHandler("lines/read", retriveSnippet);

        retrieveAllResult.andDo(documentation).andDo(print());
    }

    @DisplayName("지하철 단일 노선 받기")
    @Test
    public void retrieveLine() throws Exception {
        Line greenLine = new Line(1L, "2호선", LocalTime.of(5, 0), LocalTime.of(23, 30), 30);

        given(lineRepository.findById(greenLine.getId())).willReturn(Optional.of(greenLine));

        ResultActions retrieveResult = lineControllerTest.retrieveTest("/lines/{id}", greenLine.getId());
        RestDocumentationResultHandler documentation = retrieveDocumentationResultHandler(
                "lines/{id}/read",
                PATH_PARAMETER_SNIPPET,
                RESPONSE_FIELD_SNIPPET
        );
        retrieveResult.andDo(documentation).andDo(print());
    }

    @DisplayName("지하철 단일 노선 삭제")
    @Test
    public void deleteLine() throws Exception {
        ResultActions deleteResult = lineControllerTest.deleteTest("/lines/{id}", 1L);

        RestDocumentationResultHandler documentation =
                deleteDocumentationResultHandler("lines/{id}/delete", PATH_PARAMETER_SNIPPET);

        deleteResult.andDo(documentation).andDo(print());
    }

    @DisplayName("지하철역 구간 등록")
    @Test
    public void createEdge() throws Exception {
        Line line = new Line(1L, "2호선", LocalTime.of(5, 0), LocalTime.of(23, 30), 30);

        Station gangnam = new Station(1L, "강남역");
        Station yuksam = new Station(2L, "역삼역");
        int distance = 10;

        String inputJson = "{\"sourceId\":" + gangnam.getId() +
                ",\"targetId\":" + yuksam.getId() +
                ",\"distance\":" + distance + "}";

        ResultActions createResult = lineControllerTest.createTest("/lines/{id}/edges",line.getId(), inputJson);
        RequestFieldsSnippet requestFieldsSnippet =
                requestFields(
                        fieldWithPath("sourceId").type(JsonFieldType.NUMBER).description("The line start time"),
                        fieldWithPath("targetId").type(JsonFieldType.NUMBER).description("The line end time"),
                        fieldWithPath("distance").type(JsonFieldType.NUMBER).description("The interval between lines")
                );
        RestDocumentationResultHandler documentation =
                createDocumentationResultHandler("lines/{id}/edges/create", PATH_PARAMETER_SNIPPET, requestFieldsSnippet);
        createResult.andDo(documentation).andDo(print());
    }

    @DisplayName("지하철역 구간에서 지하철역 삭제")
    @Test
    public void deleteEdge() throws Exception {
        ResultActions deleteResult = lineControllerTest.deleteTest("/lines/{id}/edges?stationId={stationId}", 1L, 1L);

        RequestParametersSnippet requestParametersSnippet = requestParameters(
                parameterWithName("stationId").description("The station id to delete")
        );
        RestDocumentationResultHandler documentation =
                deleteDocumentationResultHandler("lines/{id}/edges/delete",PATH_PARAMETER_SNIPPET, requestParametersSnippet);

        deleteResult.andDo(documentation).andDo(print());
    }
}
