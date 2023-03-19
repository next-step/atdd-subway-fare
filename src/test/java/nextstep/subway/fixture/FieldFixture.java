package nextstep.subway.fixture;


public enum FieldFixture {
    식별자_아이디("id", "id", "식별자 아이디"),
    데이터_생성_결과_로케이션("Location", "Location", "리소스 생성 위치"),

    역_아이디("stationId", "stationId", "역 아이디"),
    역_이름("name", "name", "역 이름"),

    노선_이름("name", "name", "노선 이름"),
    노선_색깔("color", "color", "노선 색깔"),
    노선_상행_종점역_ID("upStationId", "upStationId", "상행역 아이디"),
    노선_하행_종점역_ID("downStationId", "downStationId", "하행역 아이디"),

    노선_내_역_목록("stations", "stations", "노선 내 역 목록"),
    노선_내_역_아이디_목록("stations.id", "stations[].id", "노선 내 역 아이디"),
    노선_내_역_이름_목록("stations.name", "stations[].name", "노선 내 역 이름"),
    구간_거리("distance", "distance", "구간 거리"),
    구간_소요시간("duration", "duration", "구간 소요시간"),


    경로_내_역_목록("stations", "stations", "경로 내 역 목록"),

    경로_내_역_아이디_목록("stations.id", "stations[].id", "경로 내 역 아이디"),
    경로_내_역_이름_목록("stations.name", "stations[].name", "경로 내 역 이름"),
    경로_조회_출발지_아이디("source", "source", "출발지 역"),
    경로_조회_도착지_아이디("target", "target", "도착지 역"),
    경로_조회_타입("type", "type", "경로 조회 타입"),
    경로_요금("price", "price", "경로 요금"),
    ;

    private final String field;
    private final String documentField;
    private final String description;


    FieldFixture(String field, String documentField, String description) {
        this.field = field;
        this.documentField = documentField;
        this.description = description;
    }

    public String 필드명() {
        return field;
    }

    public String 문서_필드명() {
        return documentField;
    }

    public String 필드_설명() {
        return description;
    }
}
