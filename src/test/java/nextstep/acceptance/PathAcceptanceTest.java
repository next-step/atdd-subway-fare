package nextstep.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.acceptance.commonStep.*;
import nextstep.domain.subway.PathType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Stream;

import static nextstep.acceptance.commonStep.MemberSteps.회원_생성_요청;
import static nextstep.acceptance.commonStep.PathStep.*;
import static nextstep.acceptance.commonStep.TokenStep.로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로 조회 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;
    private Long 남부터미널역;
    private Long 여의도역;
    private Long 노량진역;
    private Long 흑석역;
    private Long 동작역;

    private Long 교대강남구간거리;
    private Long 강남양재구간거리;
    private Long 양재남부터미널구간거리;
    private Long 남부터미널교대구간거리;
    private Long 여의도노량진구간거리;
    private Long 노량진흑석구간거리;
    private Long 흑석동작구간거리;

    private Long 교대강남구간시간;
    private Long 강남양재구간시간;
    private Long 양재남부터미널구간시간;
    private Long 남부터미널교대구간시간;
    private Long 여의도노량진구간시간;
    private Long 노량진흑석구간시간;
    private Long 흑석동작구간시간;

    private Long 이호선;
    private Long 삼호선;
    private Long 신분당선;
    private Long 구호선;

    private int 여의도노량진구간요금;
    private int 노량진흑석구간요금;
    private int 흑석동작구간요금;

    private int 이호선추가요금;
    private int 삼호선추가요금;
    private int 신분당선추가요금;
    private int 구호선추가요금;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*   // 여의도역  --- *9호선* --- 노량진역 --- *9호선* --- 흑석역  --- *9호선* ---   동작역
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setGivenData() {

        교대역 =  StationStep.지하철역_생성("교대역").jsonPath().getLong("id");
        강남역 =  StationStep.지하철역_생성("강남역").jsonPath().getLong("id");
        양재역 =  StationStep.지하철역_생성("양재역").jsonPath().getLong("id");
        남부터미널역 =  StationStep.지하철역_생성("남부터미널역").jsonPath().getLong("id");
        여의도역 =  StationStep.지하철역_생성("여의도역").jsonPath().getLong("id");
        노량진역 =  StationStep.지하철역_생성("노량진역").jsonPath().getLong("id");
        흑석역 =  StationStep.지하철역_생성("흑석역").jsonPath().getLong("id");
        동작역 =  StationStep.지하철역_생성("동작역").jsonPath().getLong("id");

        이호선추가요금 = 200;
        삼호선추가요금 = 300;
        신분당선추가요금 = 900;
        구호선추가요금 = 0;

        이호선 =  LineStep.지하철_노선_생성( "2호선", "Green",이호선추가요금).jsonPath().getLong("id");
        삼호선 =  LineStep.지하철_노선_생성( "삼호선", "Orange",삼호선추가요금).jsonPath().getLong("id");
        신분당선 =  LineStep.지하철_노선_생성( "신분당선", "Red",신분당선추가요금).jsonPath().getLong("id");
        구호선 =  LineStep.지하철_노선_생성( "구호선", "Gold",구호선추가요금).jsonPath().getLong("id");

        교대강남구간거리 = 10L;
        강남양재구간거리 = 15L;
        양재남부터미널구간거리 = 5L;
        남부터미널교대구간거리 = 5L;
        여의도노량진구간거리 = 10L;
        노량진흑석구간거리 = 16L;
        흑석동작구간거리 = 60L;

        교대강남구간시간 = 5L;
        강남양재구간시간 = 5L;
        양재남부터미널구간시간 = 10L;
        남부터미널교대구간시간 = 15L;
        여의도노량진구간시간 = 10L;
        노량진흑석구간시간 = 10L;
        흑석동작구간시간 = 10L;

        SectionStep.지하철구간_생성(이호선,교대역,강남역,교대강남구간거리,교대강남구간시간);
        SectionStep.지하철구간_생성(신분당선,강남역,양재역,강남양재구간거리,강남양재구간시간);
        SectionStep.지하철구간_생성(삼호선,양재역,남부터미널역,양재남부터미널구간거리,양재남부터미널구간시간);
        SectionStep.지하철구간_생성(삼호선,남부터미널역,교대역,남부터미널교대구간거리,남부터미널교대구간시간);
        SectionStep.지하철구간_생성(구호선,여의도역,노량진역,여의도노량진구간거리,여의도노량진구간시간);
        SectionStep.지하철구간_생성(구호선,노량진역,흑석역,노량진흑석구간거리,노량진흑석구간시간);
        SectionStep.지하철구간_생성(구호선,흑석역,동작역,흑석동작구간거리,흑석동작구간시간);

        여의도노량진구간요금 = 1250;
        노량진흑석구간요금 = 1250+200;
        흑석동작구간요금 = 1250+800+200;

    }

    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 거리 기준으로 지하철 경로를 조회하면
     * Then 출발역으로부터 도착역까지의 경로에 있는 역 목록과 조회한 경로 구간의 거리를 조회할 수 있다.
     */
    @DisplayName("거리 기준 지하철 경로 조회")
    @Test
    void getPathByDistance() {


        ExtractableResponse<Response> response = 지하철_경로_조회(교대역,양재역, PathType.DISTANCE);


        지하철_경로_조회_검증(response, List.of(교대역, 남부터미널역, 양재역), 남부터미널교대구간거리 + 양재남부터미널구간거리, 남부터미널교대구간시간 + 양재남부터미널구간시간);

    }

    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 시간 기준으로 지하철 경로를 조회하면
     * Then 출발역으로부터 도착역까지의 경로에 있는 역 목록과 조회한 경로 구간의 거리를 조회할 수 있다.
     */
    @DisplayName("시간 기준 지하철 경로 조회")
    @Test
    void getPathByTime() {


        ExtractableResponse<Response> response = 지하철_경로_조회(교대역,양재역, PathType.DURATION);


        지하철_경로_조회_검증(response, List.of(교대역, 강남역, 양재역), 교대강남구간거리 + 강남양재구간거리, 교대강남구간시간 + 강남양재구간시간);

    }


    /**
     * 여의도역  --- *거리 : 10KM* --- 노량진역 --- *거리 : 16KM* --- 흑석역  --- *거리 : 60KM* --- 동작역
     */

    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 거리 기준으로 지하철 경로를 조회하면
     * Then 출발역으로부터 도착역까지의 최단경로의 요금을 알 수 있다.
     */

    @DisplayName("지하철 경로 최단거리 기준 요금 조회")
    @Test
    void getPathAndPriceByDistance() {

        //when
        ExtractableResponse<Response> 기본요금응답 = 지하철_경로_조회(여의도역,노량진역, PathType.DISTANCE);
        ExtractableResponse<Response> 일차요금구간응답 = 지하철_경로_조회(노량진역,흑석역, PathType.DISTANCE);
        ExtractableResponse<Response> 이차요금구간응답 = 지하철_경로_조회(흑석역,동작역, PathType.DISTANCE);

        //then
        assertThat(기본요금응답.jsonPath().getInt("fare")).isEqualTo(여의도노량진구간요금);
        assertThat(일차요금구간응답.jsonPath().getInt("fare")).isEqualTo(노량진흑석구간요금);
        assertThat(이차요금구간응답.jsonPath().getInt("fare")).isEqualTo(흑석동작구간요금);

    }

    /**
     * 교대역    --- *2호선* ---   강남역  --- *신분당선 * ---   양재역
     */

    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 거리 기준으로 지하철 경로를 조회하면
     * Then 출발역으로부터 도착역까지의 최단경로의 요금과 노선 별 추가요금을 알 수 있다.
     */
    @DisplayName("노선별 추가요금 테스트")
    @Test
    void getPathAndPriceByLine() {
        //given
        int 교대강남양재거리기준요금 = 1250+300;

        //when
        ExtractableResponse<Response> response = 지하철_경로_조회(교대역,양재역, PathType.DURATION);

        //then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(교대강남양재거리기준요금+신분당선추가요금);

    }

    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 로그인한 회원이 거리 기준으로 지하철 경로를 조회하면
     * Then 출발역으로부터 도착역까지의 최단경로의 요금과 연령 별 할인요금을 알 수 있다.
     */
    @DisplayName("연령별 요금할인 테스트")
    @ParameterizedTest
    @MethodSource("provideMemberInfo")
    void getPathAndPriceByAge(String email,String password,int age,int expectedFare) {
        //given
        회원_생성_요청(email, password, age);
        String accessToken = 로그인_요청(email, password).jsonPath().getString("accessToken");

        //when
        ExtractableResponse<Response> response = 지하철_경로_조회(교대역,양재역, PathType.DURATION,accessToken);

        //then
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(expectedFare);

    }

    private static Stream<Arguments> provideMemberInfo() {
        return Stream.of(
                Arguments.of("child@email.com","password",10,(2450-350)*0.5),
                Arguments.of("teenager@email.com","password",15,(2450-350)*0.8),
                Arguments.of("adult@email.com","password",30,2450)
        );
    }


    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 지하철 경로 조회 시 출발역과 도착역이 같으면
     * Then Bad Request 400 error가 발생한다
     */
    @DisplayName("출발역과 도착역이 동일한 경로 조회")
    @Test
    void getPathWithIdenticalSourceAndTarget() {

        // when
        ExtractableResponse<Response> response = 지하철_경로_조회(교대역,교대역, PathType.DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 지하철 경로 조회 시 출발역과 도착역이 연결되어 있지 않으면
     * Then Bad Request 400 error가 발생한다
     */
    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경로 조회")
    @Test
    void getPathNotConnected() {

        // when
        ExtractableResponse<Response> response = 지하철_경로_조회(교대역,동작역, PathType.DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 지하철 경로 조회 시 출발역 혹은 도착역이 존재하지 않으면
     * Then 에러가 발생한다
     */

    @DisplayName("출발역 혹은 도착역이 존재하지 않는 경로 조회")
    @Test
    void getPathWithNotExistStation() {

        // when
        ExtractableResponse<Response> responseNotExistSourse = 지하철_경로_조회(동작역+1,교대역, PathType.DISTANCE);
        ExtractableResponse<Response> responseNotExistTarget = 지하철_경로_조회(교대역,동작역+1, PathType.DISTANCE);

        // then
        assertThat(responseNotExistSourse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(responseNotExistTarget.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }



}
