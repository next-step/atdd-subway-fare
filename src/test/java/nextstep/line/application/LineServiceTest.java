package nextstep.line.application;

import nextstep.common.fixture.LineFactory;
import nextstep.common.fixture.SectionFactory;
import nextstep.common.fixture.StationFactory;
import nextstep.line.application.dto.SectionCreateRequest;
import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.line.exception.LineNotExistException;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@Transactional
public class LineServiceTest {
    private final Long NOT_EXIST_LINE_ID = Long.MAX_VALUE;

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    private static final int 강남역_선릉역_길이 = 10;
    private static final int 강남역_선릉역_시간 = 2;
    private static final int 선릉역_역삼역_길이 = 20;
    private static final int 선릉역_역삼역_시간 = 3;
    private Station 강남역;
    private Station 선릉역;
    private Station 역삼역;
    private Line line;
    private SectionCreateRequest sectionCreateRequest;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(StationFactory.createStation("강남역"));
        선릉역 = stationRepository.save(StationFactory.createStation("선릉역"));
        역삼역 = stationRepository.save(StationFactory.createStation("역삼역"));
        line = lineRepository.save(LineFactory.createLine("이호선", "연두색", SectionFactory.createSection(강남역, 선릉역, 강남역_선릉역_길이, 강남역_선릉역_시간)));
        sectionCreateRequest = new SectionCreateRequest(선릉역.getId(), 역삼역.getId(), 선릉역_역삼역_길이, 선릉역_역삼역_시간);
    }

    @Nested
    @DisplayName("addSection 테스트")
    class AddSection {
        @Test
        @DisplayName("section 을 추가 할 수 있다.")
        void addSection() {
            // when
            lineService.addSection(line.getId(), sectionCreateRequest);

            // then
            assertSoftly(softly -> {
                softly.assertThat(line.getDistance()).isEqualTo(강남역_선릉역_길이 + 선릉역_역삼역_길이);
                softly.assertThat(line.getDuration()).isEqualTo(강남역_선릉역_시간 + 선릉역_역삼역_시간);
                softly.assertThat(line.getStations()).containsExactly(강남역, 선릉역, 역삼역);
            });
        }

        @Test
        @DisplayName("section 을 추가 시 line 이 존재하지 않으면 LineNotExistException 이 던져진다.")
        void addSectionFailWhenLineNotExistTest() {
            // when then
            assertThatThrownBy(() -> lineService.addSection(NOT_EXIST_LINE_ID, sectionCreateRequest))
                    .isInstanceOf(LineNotExistException.class);
        }
    }

    @Nested
    @DisplayName("removeSection 테스트")
    class RemoveSection {

        @BeforeEach
        void setUp() {
            lineService.addSection(line.getId(), sectionCreateRequest);
        }

        @Test
        @DisplayName("section 을 제거 할 수 있다.")
        void removeSection() {
            // when
            lineService.removeSection(line.getId(), 역삼역.getId());

            // then
            assertSoftly(softly -> {
                softly.assertThat(line.getDistance()).isEqualTo(강남역_선릉역_길이);
                softly.assertThat(line.getDuration()).isEqualTo(강남역_선릉역_시간);
                softly.assertThat(line.getStations()).containsExactly(강남역, 선릉역);
            });
        }

        @Test
        @DisplayName("section 을 제거 시 line 이 존재하지 않으면 LineNotExistException 이 던져진다.")
        void removeSectionFailWhenLineNotExistTest() {
            // when then
            assertThatThrownBy(() -> lineService.removeSection(NOT_EXIST_LINE_ID, 역삼역.getId()))
                    .isInstanceOf(LineNotExistException.class);
        }
    }

}
