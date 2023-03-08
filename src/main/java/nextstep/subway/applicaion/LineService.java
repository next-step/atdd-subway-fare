package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest request) {
        final Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        if (isValidSaveRequest(request)) {
            Station upStation = stationService.findById(request.getUpStationId());
            Station downStation = stationService.findById(request.getDownStationId());
            line.addSection(upStation, downStation, request.getDistance(), request.getDuration());
        }
        return LineResponse.of(line);
    }

    private boolean isValidSaveRequest(final LineRequest request) {
        return isNotNullStationId(request) && isValidDistanceAndDuration(request);
    }

    private boolean isValidDistanceAndDuration(final LineRequest request) {
        return request.getDistance() > 0 && request.getDuration() > 0;
    }

    private boolean isNotNullStationId(final LineRequest request) {
        return request.getUpStationId() != null && request.getDownStationId() != null;
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public List<LineResponse> findLineResponses() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        return LineResponse.of(findById(id));
    }

    public Line findById(Long id) {
        return lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = findById(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(final Long lineId,
                           final SectionRequest sectionRequest) {
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        Line line = findById(lineId);

        line.addSection(upStation, downStation, sectionRequest.getDistance(), sectionRequest.getDuration());
    }

    private List<StationResponse> createStationResponses(Line line) {
        return line.getStations().stream()
                .map(it -> stationService.createStationResponse(it))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findById(lineId);
        Station station = stationService.findById(stationId);

        line.deleteSection(station);
    }
}
