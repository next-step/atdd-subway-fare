package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exception.CannotRemoveSectionException;
import nextstep.subway.line.exception.LineAlreadyExistException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("노선 비즈니스 로직 단위 테스트")
public class LineServiceTest extends IntegrationTest {

    @Autowired
    private LineService lineService;

    private LineRequest line2Request;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // given
        line2Request = new LineRequest("2호선", "bg-green-600", savedStationGangNam.getId(), savedStationYeokSam.getId(), 5, 5);
    }

    @Test
    @DisplayName("노선 저장")
    void saveLine() {
        // when
        LineResponse savedLineResponse = lineService.saveLine(line2Request);

        // then
        assertThat(savedLineResponse).isNotNull();
        assertThat(savedLineResponse.getName()).isEqualTo("2호선");
    }

    @Test
    @DisplayName("노선 저장 시 존재하는 이름이 있으면 에러 발생")
    void validateNameToSaveLine() {
        // given
        lineService.saveLine(line2Request);

        // when & then
        assertThatExceptionOfType(LineAlreadyExistException.class)
                .isThrownBy(() -> {
                    LineRequest line2Request2 = new LineRequest("2호선", "bg-green-600");
                    lineService.saveLine(line2Request2);
                });
    }

    @Test
    @DisplayName("노선 수정")
    void updateLine() {
        // given
        LineResponse savedLine2Response = lineService.saveLine(line2Request);

        // when
        LineResponse updatedLine2Response = lineService.updateLine(savedLine2Response.getId(), new LineRequest("2호선", "bg-green-100"));

        // then
        assertThat(updatedLine2Response.getColor()).isEqualTo("bg-green-100");
    }

    @Test
    @DisplayName("노선 삭제")
    void deleteLine() {
        // given
        LineResponse savedLine2Response = lineService.saveLine(line2Request);

        // when
        lineService.deleteLineById(savedLine2Response.getId());

        // then
        List<LineResponse> lineResponses = getLineResponses();
        assertThat(lineResponses).hasSize(0);
    }

    @Test
    @DisplayName("모든 노선 조회")
    void findAllLines() {
        // given
        lineService.saveLine(line2Request);

        LineRequest lineNewBundangRequest = new LineRequest("신분당선", "bg-red-600", savedStationGangNam.getId(), savedStationYangJae.getId(), 5, 5);
        lineService.saveLine(lineNewBundangRequest);

        // when
        List<LineResponse> lineResponses = getLineResponses();

        // then
        assertThat(lineResponses).hasSize(2);
    }

    @Test
    @DisplayName("노선에 신규 상행역 구간 추가")
    void addSectionInUp() {
        // given
        LineResponse savedLineResponse = lineService.saveLine(line2Request);

        // when
        lineService.addSectionToLine(savedLineResponse.getId(), createSectionRequest(savedStationGyoDae, savedStationGangNam, 7, 7));

        // then
        Line line = lineService.findLineById(savedLineResponse.getId());
        assertThat(line.getStations()).containsExactlyElementsOf(Arrays.asList(savedStationGyoDae, savedStationGangNam, savedStationYeokSam));
    }

    @Test
    @DisplayName("노선 중간에 신규 구간 추가")
    void addSectionInMiddle() {
        // given
        LineResponse savedLineResponse = lineService.saveLine(line2Request);
        lineService.addSectionToLine(savedLineResponse.getId(), createSectionRequest(savedStationYeokSam, savedStationSamseong, 8, 8));

        // when
        lineService.addSectionToLine(savedLineResponse.getId(), createSectionRequest(savedStationYeokSam, savedStationSeolleung, 4, 4));

        // then
        Line line = lineService.findLineById(savedLineResponse.getId());
        assertThat(line.getStations()).containsExactlyElementsOf(Arrays.asList(savedStationGangNam, savedStationYeokSam, savedStationSeolleung, savedStationSamseong));
    }

    @Test
    @DisplayName("노선의 하행역에 신규 구간 추가")
    void addSectionInDown() {
        // given
        LineResponse savedLineResponse = lineService.saveLine(line2Request);

        // when
        lineService.addSectionToLine(savedLineResponse.getId(), createSectionRequest(savedStationYeokSam, savedStationSamseong, 8, 8));

        // then
        Line line = lineService.findLineById(savedLineResponse.getId());
        assertThat(line.getStations()).containsExactlyElementsOf(Arrays.asList(savedStationGangNam, savedStationYeokSam, savedStationSamseong));
    }

    @Test
    @DisplayName("노선에 있는 상행 종점역 구간 제거")
    void removeUpStationSection() {
        // given
        LineResponse savedLineResponse = lineService.saveLine(line2Request);
        savedLineResponse = lineService.addSectionToLine(savedLineResponse.getId(), createSectionRequest(savedStationYeokSam, savedStationSamseong, 8, 8));

        // when
        lineService.removeSectionToLine(savedLineResponse.getId(), savedStationGangNam.getId());

        // then
        Line resultLine = lineService.findLineById(savedLineResponse.getId());
        assertThat(resultLine.getSections()).hasSize(1);
    }

    @Test
    @DisplayName("노선에 있는 중간 구간 제거")
    void removeMiddleSection() {
        // given
        LineResponse savedLineResponse = lineService.saveLine(line2Request);
        savedLineResponse = lineService.addSectionToLine(savedLineResponse.getId(), createSectionRequest(savedStationYeokSam, savedStationSamseong, 8, 8));
        savedLineResponse = lineService.addSectionToLine(savedLineResponse.getId(), createSectionRequest(savedStationYeokSam, savedStationSeolleung, 4, 4));

        // when
        lineService.removeSectionToLine(savedLineResponse.getId(), savedStationYeokSam.getId());

        // then
        Line resultLine = lineService.findLineById(savedLineResponse.getId());
        assertThat(resultLine.getSections()).hasSize(2);
    }

    @Test
    @DisplayName("노선에 있는 하행 종점역 구간 제거")
    void removeDownStationSection() {
        // given
        LineResponse savedLineResponse = lineService.saveLine(line2Request);

        savedLineResponse = lineService.addSectionToLine(savedLineResponse.getId(), createSectionRequest(savedStationYeokSam, savedStationSamseong, 8, 8));

        // when
        lineService.removeSectionToLine(savedLineResponse.getId(), savedStationSamseong.getId());

        // then
        Line resultLine = lineService.findLineById(savedLineResponse.getId());
        assertThat(resultLine.getSections()).hasSize(1);
    }

    @Test
    @DisplayName("노선에 구간 삭제 시 구간이 1개만 있을 경우 에러 발생")
    void validateSectionSizeToDeleteSection() {
        // given
        LineResponse savedLineResponse = lineService.saveLine(line2Request);

        // when & then
        assertThatExceptionOfType(CannotRemoveSectionException.class)
                .isThrownBy(() -> lineService.removeSectionToLine(savedLineResponse.getId(), savedStationYeokSam.getId()));
    }

    private SectionRequest createSectionRequest(Station upStation, Station downStation, int distance, int duration) {
        return new SectionRequest(upStation.getId(), downStation.getId(), distance, duration);
    }

    private List<LineResponse> getLineResponses() {
        return lineService.findAllLines().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }
}
