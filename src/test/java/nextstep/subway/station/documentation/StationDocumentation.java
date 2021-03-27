package nextstep.subway.station.documentation;

import io.restassured.RestAssured;
import nextstep.subway.Documentation;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class StationDocumentation extends Documentation {

    private Map<String, String> createParams;
    private StationResponse 강남역;

    @BeforeEach
    void setUp() {
        createParams = new HashMap<>();
        createParams.put("name", "신촌역");

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);

    }

    @Test
    void createStation() {
        RestAssured
                .given(spec).log().all()
                .filter(document("stations/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                 ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createParams)
                .when().post("/stations")
                .then().log().all().extract();
    }

    @Test
    void deleteStation() {
        RestAssured
                .given(spec).log().all()
                .filter(document("stations/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/{id}", 강남역.getId())
                .then().log().all().extract();
    }



}
