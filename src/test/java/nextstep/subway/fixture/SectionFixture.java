package nextstep.subway.fixture;

import nextstep.subway.applicaion.dto.request.SectionRequest;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.fixture.FieldFixture.구간_거리;
import static nextstep.subway.fixture.FieldFixture.구간_소요시간;
import static nextstep.subway.fixture.FieldFixture.노선_상행_종점역_ID;
import static nextstep.subway.fixture.FieldFixture.노선_하행_종점역_ID;
import static nextstep.subway.fixture.StationFixture.강남역;
import static nextstep.subway.fixture.StationFixture.교대역;
import static nextstep.subway.fixture.StationFixture.남부터미널역;
import static nextstep.subway.fixture.StationFixture.삼성역;
import static nextstep.subway.fixture.StationFixture.선릉역;
import static nextstep.subway.fixture.StationFixture.양재역;
import static nextstep.subway.fixture.StationFixture.역삼역;
import static nextstep.subway.fixture.StationFixture.정자역;

public enum SectionFixture {

    /** [구간 거리] (소요시간은 거리 x 2)
     *
     *   교대역     --- (6) --- 강남역 --- (7) --- 역삼역 --- (8) --- 선릉역 --- (5) --- 삼성역
     *     |                     |
     *    (3)                   (10)
     *     |                     |
     * 남부터미널역  --- (7) --- 양재역
     *                           |
     *                          (6)
     *                           |
     *                         정자역
     */
    교대_남부터미널_구간(3, 6, 교대역, 남부터미널역),
    남부터미널_양재_구간(7, 14, 남부터미널역, 양재역),
    교대_강남_구간(6, 12, 교대역, 강남역),
    강남_역삼_구간(7, 14, 강남역, 역삼역),
    역삼_선릉_구간(8, 16, 역삼역, 선릉역),
    선릉_삼성_구간(5, 10, 선릉역, 삼성역),
    강남_삼성_구간(7 + 8 + 5, (7 + 8 + 5) * 2, 강남역, 삼성역),
    역삼_삼성_구간(8 + 5, (8 + 5) * 2, 역삼역, 삼성역),
    강남_양재_구간(10, 20, 강남역, 양재역),
    양재_정자_구간(6, 12, 양재역, 정자역),
    강남_정자_구간(10 + 6, (10 + 6) * 2, 강남역, 정자역),


    강남_역삼_구간_비정상_거리(100, 200, 강남역, 역삼역),
    ;

    private final int distance;
    private final int duration;
    private final StationFixture upStation;
    private final StationFixture downStation;

    SectionFixture(int distance, int duration, StationFixture upStation, StationFixture downStation) {
        this.distance = distance;
        this.duration = duration;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public int 구간_거리() {
        return distance;
    }

    public int 구간_소요시간() {
        return duration;
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
        params.put(구간_거리.필드명(), String.valueOf(구간_거리()));
        params.put(구간_소요시간.필드명(), String.valueOf(구간_소요시간()));
        return params;
    }

    public SectionRequest 구간_요청_DTO_생성(Long 상행역_ID, Long 하행역_ID) {
        return new SectionRequest(상행역_ID, 하행역_ID, 구간_거리(), 구간_소요시간());
    }
}
