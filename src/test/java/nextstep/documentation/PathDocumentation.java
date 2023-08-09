package nextstep.documentation;

import nextstep.acceptance.commonStep.LineStep;
import nextstep.acceptance.commonStep.SectionStep;
import nextstep.acceptance.commonStep.StationStep;
import nextstep.domain.Path;
import nextstep.domain.Sections;
import nextstep.domain.Station;
import nextstep.domain.subway.PathType;
import nextstep.dto.PathResponse;
import nextstep.service.PathService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static nextstep.acceptance.commonStep.PathStep.지하철_경로_조회;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class PathDocumentation extends Documentation {

    private Long 교대역;
    private Long 강남역;
    private Long 양재역;

    private Long 교대강남구간거리;
    private Long 강남양재구간거리;
    private Long 교대강남구간시간;
    private Long 강남양재구간시간;

    private Long 이호선;
    private Long 삼호선;
    private Long 신분당선;

    @BeforeEach
    public void setGivenData() {

        교대역 =  StationStep.지하철역_생성("교대역").jsonPath().getLong("id");
        강남역 =  StationStep.지하철역_생성("강남역").jsonPath().getLong("id");
        양재역 =  StationStep.지하철역_생성("양재역").jsonPath().getLong("id");


        이호선 =  LineStep.지하철_노선_생성( "2호선", "Green").jsonPath().getLong("id");
        삼호선 =  LineStep.지하철_노선_생성( "삼호선", "Orange").jsonPath().getLong("id");
        신분당선 =  LineStep.지하철_노선_생성( "신분당선", "Red").jsonPath().getLong("id");

        교대강남구간거리 = 10L;
        강남양재구간거리 = 15L;

        교대강남구간시간 = 5L;
        강남양재구간시간 = 5L;

        SectionStep.지하철구간_생성(이호선,교대역,강남역,교대강남구간거리,교대강남구간시간);
        SectionStep.지하철구간_생성(신분당선,강남역,양재역,강남양재구간거리,강남양재구간시간);


    }
    @Test
    void path() {

        지하철_경로_조회(교대역,양재역, PathType.DISTANCE.getType(),createRequestSpecification("path"));

    }
}
