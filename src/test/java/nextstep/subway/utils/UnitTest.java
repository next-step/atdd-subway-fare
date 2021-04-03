package nextstep.subway.utils;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UnitTest {

    protected static final int ADULT_MEMBER_AGE = 20;
    protected static final int YOUTH_MEMBER_AGE = 17;
    protected static final int CHILD_MEMBER_AGE = 7;

    @Autowired
    protected StationRepository stationRepository;

    protected Station savedStationGangNam;
    protected Station savedStationYeokSam;
    protected Station savedStationSeolleung;
    protected Station savedStationSamseong;

    protected Station savedStationYangJae;
    protected Station savedStationYangJaeCitizensForest;
    protected Station savedStationCheonggyesan;

    protected Station savedStationGyoDae;
    protected Station savedStationNambuTerminal;

    protected Station savedStationMyeongDong;

    @BeforeEach
    public void setUp() {
        // given
        savedStationGangNam = stationRepository.save(new Station("강남역"));
        savedStationYeokSam = stationRepository.save(new Station("역삼역"));
        savedStationSeolleung = stationRepository.save(new Station("선릉역"));
        savedStationSamseong = stationRepository.save(new Station("삼성역"));

        savedStationYangJae = stationRepository.save(new Station("양재역"));
        savedStationYangJaeCitizensForest = stationRepository.save(new Station("양재시민의숲역"));
        savedStationCheonggyesan = stationRepository.save(new Station("청계산입구역"));

        savedStationGyoDae = stationRepository.save(new Station("교대역"));
        savedStationNambuTerminal = stationRepository.save(new Station("남부터미널역"));

        savedStationMyeongDong = stationRepository.save(new Station("명동역"));
    }
}
