package nextstep.subway.unit;

import nextstep.subway.acceptance.ApplicationContextTest;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static nextstep.subway.acceptance.LineSteps.추가요금;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("노선 관련 테스트")
@Transactional
class LineServiceTest extends ApplicationContextTest {
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private LineService lineService;

    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));
        삼성역 = stationRepository.save(new Station("삼성역"));
    }

    @DisplayName("노선의 구간을 생성한다.")
    @Test
    void addSection() {

        final Line 이호선 = createLine("2호선", "green", 추가요금(900));
        이호선.addSection(강남역, 역삼역, 5, 10);

        lineService.addSection(이호선.getId(), new SectionRequest(역삼역.getId(), 삼성역.getId(), 10, 20));

        Line line = lineService.findById(이호선.getId());
        assertAll(
                () -> assertThat(line.getSections()).hasSize(2),
                () -> assertThat(line.getExtraFare()).isEqualTo(Fare.from(900))
        );
    }

    @DisplayName("노선 목록을 조회한다.")
    @Test
    void showLines() {
        createLine("2호선", "green", 추가요금(900));
        createLine("3호선", "red", 추가요금(500));

        final List<LineResponse> 응답_노선_목록 = lineService.findLineResponses();

        assertAll(
                () -> assertThat(응답_노선_목록).hasSize(2),
                () -> assertThat(응답_노선_목록.get(0).getName()).isEqualTo("2호선"),
                () -> assertThat(응답_노선_목록.get(0).getColor()).isEqualTo("green"),
                () -> assertThat(응답_노선_목록.get(0).getExtraFare()).isEqualTo(BigDecimal.valueOf(900)),
                () -> assertThat(응답_노선_목록.get(1).getName()).isEqualTo("3호선"),
                () -> assertThat(응답_노선_목록.get(1).getColor()).isEqualTo("red"),
                () -> assertThat(응답_노선_목록.get(1).getExtraFare()).isEqualTo(BigDecimal.valueOf(500))
        );
    }

    @DisplayName("특정 노선을 조회한다.")
    @Test
    void findLine() {
        final Line 노선_이호선 = lineRepository.save(createLine("2호선", "green", 추가요금(900)));
        노선_이호선.addSection(강남역, 역삼역, 10, 20);

        final LineResponse 응답_노선 = lineService.findLineResponseById(노선_이호선.getId());

        assertAll(
                () -> assertThat(응답_노선.getId()).isEqualTo(노선_이호선.getId()),
                () -> assertThat(응답_노선.getName()).isEqualTo("2호선"),
                () -> assertThat(응답_노선.getColor()).isEqualTo("green"),
                () -> assertThat(응답_노선.getExtraFare()).isEqualTo(BigDecimal.valueOf(900)),
                () -> assertThat(응답_노선.getStations()).hasSize(2)
        );
    }

    @DisplayName("노선을 수정한다.")
    @Test
    void updateLine() {
        final LineRequest 요청_수정_노선 = 노선_수정_생성("신분당선", "red", BigDecimal.valueOf(500));
        final Line 노선 = lineRepository.save(createLine("2호선", "green", 추가요금(900)));

        lineService.updateLine(노선.getId(), 요청_수정_노선);

        assertAll(
                () -> assertThat(노선.getName()).isEqualTo("신분당선"),
                () -> assertThat(노선.getColor()).isEqualTo("red"),
                () -> assertThat(노선.getExtraFare()).isEqualTo(Fare.from(500))
        );
    }

    @DisplayName("노선을 삭제한다.")
    @Test
    void deleteLine() {
        final Line 노선 = lineRepository.save(createLine("2호선", "green"));

        lineService.deleteLine(노선.getId());

        assertThatThrownBy(() -> lineService.findById(노선.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Line createLine(final String name, final String color) {
        return lineRepository.save(new Line(name, color));
    }

    private Line createLine(final String name, final String color, final BigDecimal fare) {
        return lineRepository.save(new Line(name, color, fare));
    }

    private static LineRequest 노선_수정_생성(final String name, final String color, final BigDecimal fare) {
        final LineRequest lineRequest = new LineRequest();
        reflectionByObject(name, "name", lineRequest);
        reflectionByObject(color, "color", lineRequest);
        reflectionByObject(fare, "extraFare", lineRequest);
        return lineRequest;
    }

    public static void reflectionByObject(final Object field, final String filedName, final Object object) {
        ReflectionTestUtils.setField(object, filedName, field);
    }
}
