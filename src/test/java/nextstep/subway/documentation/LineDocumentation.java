package nextstep.subway.documentation;

import io.restassured.RestAssured;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class LineDocumentation extends Documentation {

    public static final LineResponse 신분당선 = new LineResponse(
        1L,
        "신분당선",
        "red",
        Arrays.asList(new StationResponse(1L, "강남역"), new StationResponse(2L, "분당역"))
    );

    public static final LineResponse 분당선 = new LineResponse(
        2L,
        "분당선",
        "green",
        Arrays.asList(new StationResponse(1L, "강남역"), new StationResponse(3L, "혜화역"))
    );
    @MockBean
    LineService lineService;

    @Test
    void createLine() {

        when(lineService.saveLine(any())).thenReturn(신분당선);

        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "red");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");
        givenOauth().
            filter(document("line/create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all().extract();
    }


    @Test
    void showLines() {
        when(lineService.findLineResponses()).thenReturn(Arrays.asList(신분당선, 분당선));

        givenOauth()
            .filter(document("line/showLines",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .when().get("/lines")
            .then().log().all().extract();
    }

    @Test
    void getLine() {
        when(lineService.findLineResponseById(any())).thenReturn(신분당선);

        givenNotOauth()
            .filter(document("line/getLine",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .when().get("/lines/{id}", 1)
            .then().statusCode(HttpStatus.OK.value()).log().all().extract();
    }
}
