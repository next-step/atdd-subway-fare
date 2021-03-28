package nextstep.subway.path.domain;

import nextstep.repository.MemoryLineRepository;
import nextstep.repository.MemoryStationRepository;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.application.GraphService;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;

public class PathTest {
    protected StationResponse 교대역;
    protected StationResponse 강남역;
    protected StationResponse 양재역;
    protected StationResponse 남부터미널역;
    protected LineResponse 이호선;
    protected LineResponse 신분당선;
    protected LineResponse 삼호선;

    protected LineRepository lineRepository;
    protected StationRepository stationRepository;
    protected StationService stationService;
    protected LineService lineService;
    protected PathService pathService;
    protected GraphService graphService;


    @BeforeEach
    void setup() {
        lineRepository = new MemoryLineRepository();
        stationRepository = new MemoryStationRepository();
        stationService = new StationService(stationRepository);
        lineService = new LineService(lineRepository, stationService);
        graphService = new GraphService(lineService);


        교대역 = stationService.saveStation(new StationRequest("교대역"));
        강남역 = stationService.saveStation(new StationRequest("강남역"));
        양재역 = stationService.saveStation(new StationRequest("양재역"));
        남부터미널역 = stationService.saveStation(new StationRequest("남부터미널역"));

        이호선 = lineService.saveLine(new LineRequest("2호선", "green", 교대역.getId(), 강남역.getId(), 70, 70, 0));
        신분당선 = lineService.saveLine(new LineRequest("신분당선", "green", 강남역.getId(), 양재역.getId(), 7, 5, 900));
        삼호선 = lineService.saveLine(new LineRequest("3호선", "green", 교대역.getId(), 남부터미널역.getId(), 16, 17, 500));

        lineService.addSection(삼호선.getId(), new SectionRequest(남부터미널역.getId(), 양재역.getId(), 43, 30));
    }
}
