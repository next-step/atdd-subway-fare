package nextstep.units;

import nextstep.line.Line;
import nextstep.line.LineCreateRequest;
import nextstep.line.LineRepository;
import nextstep.line.LineService;
import nextstep.line.section.Section;
import nextstep.line.section.SectionAddRequest;
import nextstep.path.ArrivalTimeCalculator;
import nextstep.station.Station;
import nextstep.station.StationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ArrivalTimeCalculatorTest {

    @Autowired
    private LineService lineService;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    Long line2Id;

    Long lineShinbundangId;

    private void setUp() {
        /**
         * 2호선 역 정보: 서초역 -(3분)- 강남역 -(4분)- 역삼역 -(3분)- 선릉역
         */
        Station 서초 = stationRepository.save(new Station("서초역")); // 1L
        Station 강남 = stationRepository.save(new Station( "강남역"));// 2L
        Station 역삼 = stationRepository.save(new Station( "역삼역"));// 3L
        Station 선릉 = stationRepository.save(new Station( "선릉역"));// 4L

        line2Id = lineService.create(new LineCreateRequest("2호선", "green", 0, 3, 3, "05:00", "23:00", 9, 서초.getId(), 강남.getId())).getId();
        lineService.addSection(line2Id, new SectionAddRequest(강남.getId(), 역삼.getId(), 4, 4));
        lineService.addSection(line2Id, new SectionAddRequest(역삼.getId(), 선릉.getId(), 3, 3));

        /**
         * 신분당선 역 정보: 강남역 -(4분)- 양재역 - (3분) - 냥냥역
         */
        Station 양재 = stationRepository.save(new Station("양재역")); // 5L
        Station 냥냥 = stationRepository.save(new Station("냥냥역")); // 6L

        lineShinbundangId = lineService.create(new LineCreateRequest("신분당선", "crimson", 0, 4, 4, "05:00", "23:00", 21, 강남.getId(), 양재.getId())).getId();
        lineService.addSection(lineShinbundangId, new SectionAddRequest(양재.getId(), 냥냥.getId(), 2, 2));
    }

    @Test
    public void singleLine() {
        setUp();
        LocalTime currentTime = LocalTime.parse("10:00", DateTimeFormatter.ISO_LOCAL_TIME);
        Line line2 = lineRepository.findById(line2Id).orElseThrow();
        Section seochoToGangnam = line2.getSections().get(0);
        List<Section> sections = new ArrayList<>();
        sections.add(seochoToGangnam);

        ArrivalTimeCalculator arrivalTimeCalculator = new ArrivalTimeCalculator(sections, currentTime);
        LocalTime arrivalTime = arrivalTimeCalculator.calculate();

        assertEquals(arrivalTime, LocalTime.parse("10:09", DateTimeFormatter.ISO_LOCAL_TIME));
    }

    @Test
    public void transferLine() {
        setUp();
        LocalTime currentTime = LocalTime.parse("10:09", DateTimeFormatter.ISO_LOCAL_TIME);
        Line line = lineRepository.findById(2L).orElseThrow();
        Section gangnamToYangjae = line.getSections().get(0);
        List<Section> sections = new ArrayList<>();
        sections.add(gangnamToYangjae);

        ArrivalTimeCalculator arrivalTimeCalculator = new ArrivalTimeCalculator(sections, currentTime);
        LocalTime arrivalTime = arrivalTimeCalculator.calculate();

        assertEquals(arrivalTime, LocalTime.parse("10:19", DateTimeFormatter.ISO_LOCAL_TIME));
    }

    @Test
    public void afterLastDepartureTime() {
        setUp();
        LocalTime currentTime = LocalTime.parse("23:30", DateTimeFormatter.ISO_LOCAL_TIME);
        Line lineShinbundang = lineRepository.findById(lineShinbundangId).orElseThrow();
        Section gangnamToYangjae = lineShinbundang.getSections().get(0);
        List<Section> sections = new ArrayList<>();
        sections.add(gangnamToYangjae);

        ArrivalTimeCalculator arrivalTimeCalculator = new ArrivalTimeCalculator(sections, currentTime);
        LocalTime arrivalTime = arrivalTimeCalculator.calculate();

        assertEquals(arrivalTime, LocalTime.parse("05:04", DateTimeFormatter.ISO_LOCAL_TIME));
    }
}
