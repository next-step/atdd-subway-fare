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
    public LineResponse saveLine(LineRequest request) {
        /**
         * 상태 추가에 대해서 코드 수정을 어떻게 해야할까?
         * Line에 extraFare = 0으로 default 값 줘서 기존 코드들을 그대로 두려고 했더니 이런 상황에 대해 컴파일 에러가 발생하지 않는다
         * 기존 코드를 유지하더라도 수정할 내용을 빠뜨리지 않는 법?
         * 1. 수정하려는 기능을 현재 있는 기능들에 대해 대조하면서 영향을 미칠 수 있을지 대조해보기
         * 2. 싹다 리팩토링하기 (시간은 좀 걸리겠지만 안정성이 더 크다고 생각)
         * 3. domain이 아니라 dto에 default값 줘서 처리하기
         * - 그런데 이 방법은 비즈니스를 도메인이 아니라 dto에서 가지고 있다는 점에서 좀 꺼려짐
         *  - 근데 그렇게 따지면 dto에서 검증 로직을 가지고 있는 경우도 많지 않나?
         *
         * 여기선 1.을 채택. 한 가지 생각이 드는건 프로덕션 코드에서는 생성자를 직접 안 쓰고 팩토리 메서드만 사용하면,
         * 테스트 코드들에 대해서는 생성자 쓰면서 기존 코드들을 유지할 수 있을 것 같다. (단위 테스트의 경우)
         * 프로덕션 / 인수 테스트들은 팩토리 메서드를 수정하면 컴파일 에러가 발생할 것이므로 객체를 가져다 쓸때 컴파일러가 상태 수정하지 않으면 에러를 뱉게 하는 방법을 사용할 수 있을 것 같다.
         */
        Line line = lineRepository.save(new Line(request.getName(), request.getColor(), request.getExtraFare()));
        if (request.getUpStationId() != null && request.getDownStationId() != null && request.getDistance() != 0) {
            Station upStation = stationService.findById(request.getUpStationId());
            Station downStation = stationService.findById(request.getDownStationId());
            line.addSection(upStation, downStation, request.getDistance(), request.getDuration());
        }
        return LineResponse.of(line);
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
        line.update(lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
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
