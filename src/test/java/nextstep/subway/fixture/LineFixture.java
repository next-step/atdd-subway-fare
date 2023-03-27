package nextstep.subway.fixture;

import nextstep.subway.domain.Line;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.fixture.FieldFixture.구간_거리;
import static nextstep.subway.fixture.FieldFixture.구간_소요시간;
import static nextstep.subway.fixture.FieldFixture.노선_상행_종점역_ID;
import static nextstep.subway.fixture.FieldFixture.노선_색깔;
import static nextstep.subway.fixture.FieldFixture.노선_이름;
import static nextstep.subway.fixture.FieldFixture.노선_추가요금;
import static nextstep.subway.fixture.FieldFixture.노선_하행_종점역_ID;

public enum LineFixture {
    이호선("2호선", "green", 0),
    삼호선("3호선", "orange", 0),
    사호선("4호선", "blue", 0),
    신분당선("신분당선", "red", 900),
    ;

    private final String name;
    private final String color;
    private final int additionalFare;

    LineFixture(String name, String color, int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
    }

    public String 노선_이름() {
        return name;
    }

    public String 노선_색깔() {
        return color;
    }

    public int 추가_요금() {
        return additionalFare;
    }

    public Map<String, String> 등록_요청_데이터_생성(Long 상행종점역_id, Long 하행종점역_id, SectionFixture 구간) {
        return 등록_요청_데이터_생성(상행종점역_id, 하행종점역_id, 구간.구간_거리(), 구간.구간_소요시간());
    }

    public Map<String, String> 등록_요청_데이터_생성(Long 상행종점역_id, Long 하행종점역_id, int 구간거리, int 구간소요시간) {
        Map<String, String> params = new HashMap<>();
        params.put(노선_이름.필드명(), 노선_이름());
        params.put(노선_색깔.필드명(), 노선_색깔());
        params.put(노선_추가요금.필드명(), String.valueOf(추가_요금()));
        params.put(구간_거리.필드명(), String.valueOf(구간거리));
        params.put(구간_소요시간.필드명(), String.valueOf(구간소요시간));
        params.put(노선_상행_종점역_ID.필드명(), String.valueOf(상행종점역_id));
        params.put(노선_하행_종점역_ID.필드명(), String.valueOf(하행종점역_id));
        return params;
    }

    public Line 엔티티_생성() {
        return new Line(노선_이름(), 노선_색깔(), 추가_요금());
    }
}
