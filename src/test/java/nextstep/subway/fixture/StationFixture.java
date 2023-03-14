package nextstep.subway.fixture;


import nextstep.subway.applicaion.dto.response.StationResponse;
import nextstep.subway.domain.Station;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.fixture.FieldFixture.역_이름;

public enum StationFixture {
    교대역,
    강남역,
    역삼역,
    선릉역,
    삼성역,
    양재역,
    정자역,
    남부터미널역,
    ;

    public String 역_이름() {
        return name();
    }

    public Map<String, String> 요청_데이터_생성() {
        Map<String, String> params = new HashMap<>();
        params.put(역_이름.필드명(), 역_이름());
        return params;
    }

    public StationResponse 응답_데이터_생성(Long id) {
        return new StationResponse(id, 역_이름());
    }

    public Station 엔티티_생성() {
        return new Station(역_이름());
    }
}
