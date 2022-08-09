package nextstep.subway.documentation;

import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class StationDocumentation extends Documentation {
    @MockBean
    StationService stationService;

    @Test
    void createStation() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        when(stationService.saveStation(any())).thenReturn(new StationResponse(1L, "강남역"));

        givenOauth()
            .filter(document("station/create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/stations")
            .then().statusCode(HttpStatus.CREATED.value()).log().all().extract();
    }

    @Test
    void findStation() {
        List<StationResponse> stationResponses = Lists.newArrayList(
            new StationResponse(1L, "강남역"),
            new StationResponse(2L, "역삼역")
        );

        when(stationService.findAllStations()).thenReturn(stationResponses);

        givenOauth()
            .filter(document("station/findStations",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .when().get("/stations")
            .then().statusCode(HttpStatus.OK.value()).log().all().extract();
    }

    @Test
    void deleteStation() {
        doNothing().when(stationService).deleteStationById(any());

        givenOauth()
            .filter(document("station/deleteStation",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .when().delete("/stations/{id}", 1)
            .then().statusCode(HttpStatus.NO_CONTENT.value()).log().all().extract();
    }

}
