package nextstep.subway.fixture;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.fixture.FieldFixture.노선_간_거리;
import static nextstep.subway.fixture.FieldFixture.노선_상행_종점역_ID;
import static nextstep.subway.fixture.FieldFixture.노선_하행_종점역_ID;
import static nextstep.subway.fixture.FieldFixture.식별자_아이디;
import static nextstep.subway.fixture.StationFixture.강남역;
import static nextstep.subway.fixture.StationFixture.역삼역;

public enum SectionFixture {

    강남_역삼_구간(10, 강남역, 역삼역),

    강남_역삼_구간_비정상_거리(100, 강남역, 역삼역),
    ;

    private final int distance;
    private final StationFixture upStation;
    private final StationFixture downStation;

    SectionFixture(int distance, StationFixture upStation, StationFixture downStation) {
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public int 구간_거리() {
        return distance;
    }

    public StationFixture 상행역() {
        return upStation;
    }

    public StationFixture 하행역() {
        return downStation;
    }

    public Map<String, String> 요청_데이터_생성(Long 상행역_id, Long 하행역_id) {
        Map<String, String> params = new HashMap<>();
        params.put(노선_상행_종점역_ID.필드명(), String.valueOf(상행역_id));
        params.put(노선_하행_종점역_ID.필드명(), String.valueOf(하행역_id));
        params.put(노선_간_거리.필드명(), String.valueOf(구간_거리()));
        return params;
    }

    public Section 엔티티_생성(Line 호선) {
        return new Section(호선, 상행역().엔티티_생성(), 하행역().엔티티_생성(), 구간_거리());
    }

    public Section 엔티티_생성(Long id, Line 호선) {
        Section section = new Section(호선, 상행역().엔티티_생성(), 하행역().엔티티_생성(), 구간_거리());
        ReflectionTestUtils.setField(section, 식별자_아이디.필드명(), id);
        return section;
    }

    public Section 엔티티_생성(Line 호선, Station 상행역, Station 하행역) {
        return new Section(호선, 상행역, 하행역, 구간_거리());
    }
}
