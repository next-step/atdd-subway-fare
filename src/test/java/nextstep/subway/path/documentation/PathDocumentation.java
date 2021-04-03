package nextstep.subway.path.documentation;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.Documentation;
import nextstep.subway.line.domain.PathType;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static nextstep.subway.path.acceptance.PathSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_등록되어_있음;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PathDocumentation extends Documentation {

    @MockBean
    private PathService pathService;

    @Test
    void path() {
        //given
        PathResponse pathResponse = 응답_만들기(30, 10, 1650,
                역_만들기(1L, "강남역"),
                역_만들기(2L, "역삼역"),
                역_만들기(3L, "선릉역")
        );

        //when
        경로_탐색할_때(pathService, pathResponse);

        //then
        RequestSpecification requestSpecification = specificationForMakeDocument("path");
        두_역의_최단_거리_경로_조회를_요청(requestSpecification, 1L, 3L);
    }
}

