package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.subway.station.acceptance.StationRequestSteps.지하철_역_등록_됨;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
public abstract class AcceptanceTest {

    protected static final String ADULT_EMAIL = "adult-email@email.com";
    protected static final String YOUTH_EMAIL = "youth-email@email.com";
    protected static final String CHILD_EMAIL = "child-email@email.com";
    protected static final String PASSWORD = "password";
    protected static final int ADULT_AGE = 20;
    protected static final int YOUTH_AGE = 17;
    protected static final int CHILD_AGE = 7;

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    protected static RequestSpecification spec;
    protected BaseDocumentation baseDocumentation;

    protected StationResponse 교대역;
    protected StationResponse 강남역;
    protected StationResponse 역삼역;
    protected StationResponse 삼성역;
    protected StationResponse 양재역;
    protected StationResponse 양재시민의숲역;
    protected StationResponse 청계산입구역;
    protected StationResponse 판교역;
    protected StationResponse 남부터미널역;

    protected LineResponse 이호선;
    protected LineResponse 신분당선;
    protected LineResponse 삼호선;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        databaseCleanup.execute();

        교대역 = 지하철_역_등록_됨("교대역").as(StationResponse.class);
        강남역 = 지하철_역_등록_됨("강남역").as(StationResponse.class);
        역삼역 = 지하철_역_등록_됨("역삼역").as(StationResponse.class);
        삼성역 = 지하철_역_등록_됨("삼성역").as(StationResponse.class);
        양재역 = 지하철_역_등록_됨("양재역").as(StationResponse.class);
        판교역 = 지하철_역_등록_됨("판교역").as(StationResponse.class);

        양재시민의숲역 = 지하철_역_등록_됨("양재시민의숲역").as(StationResponse.class);
        청계산입구역 = 지하철_역_등록_됨("청계산입구역").as(StationResponse.class);
        남부터미널역 = 지하철_역_등록_됨("남부터미널역").as(StationResponse.class);

        spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();
    }
}
