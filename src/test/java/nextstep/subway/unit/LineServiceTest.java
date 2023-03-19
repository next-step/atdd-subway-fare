package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.subway.fixture.LineFixture.이호선;
import static nextstep.subway.fixture.SectionFixture.강남_역삼_구간;
import static nextstep.subway.fixture.SectionFixture.역삼_삼성_구간;
import static nextstep.subway.fixture.StationFixture.강남역;
import static nextstep.subway.fixture.StationFixture.삼성역;
import static nextstep.subway.fixture.StationFixture.역삼역;
import static nextstep.subway.unit.support.LineSupporter.구간이_N개_저장되어_있다;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class LineServiceTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @Test
    void addSection() {
        Station 강남_역 = stationRepository.save(강남역.엔티티_생성());
        Station 역삼_역 = stationRepository.save(역삼역.엔티티_생성());
        Station 삼성_역 = stationRepository.save(삼성역.엔티티_생성());
        Line 이호선 = lineRepository.save(createLine(강남_역, 역삼_역));

        lineService.addSection(이호선.getId(), 역삼_삼성_구간.구간_요청_DTO_생성(역삼_역.getId(), 삼성_역.getId()));

        Line line = lineService.findById(이호선.getId());
        구간이_N개_저장되어_있다(line, 2);
    }

    private Line createLine(Station 강남역, Station 역삼역) {
        Line line = 이호선.엔티티_생성();
        line.addSection(강남역, 역삼역, 강남_역삼_구간.구간_거리(), 강남_역삼_구간.구간_소요시간());
        return line;
    }
}
