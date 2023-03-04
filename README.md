# 지하철 노선도 미션

[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 실습 - 문서화

- [x] 경로 찾기 기능을 문서화하기 위한 테스트를 작성하세요.
- [x] 테스트 작성 후 Spring Rest Docs 적용을 통해 문서에 기재할 정보를 설정하세요.

### 실습방법

- [x] PathDocumentation의 path 메서드를 완성시키세요.
- [x] PathDocumentation의 테스트를 수행시켜 snippet을 생성하세요.
- [x] gradle로 asciidoctor task를 수행시켜 문서 파일을 생성하세요
- [x] build > asciidoc > html5 > index.html을 브라우저로 열고 캡쳐한 이미지를 PR에 포함시켜 주세요.
- [x] PathSteps 클래스를 활용하여 PathDocumentation 내 코드 중복을 제거해주세요

## 🚀 1단계 - 경로 조회 타입 추가

### 요구사항

#### 기능 요구사항 - 최소 시간 경로 타입 추가

- [ ] 경로 조회 시 최소 시간 기준으로 조회할 수 있도록 기능을 추가하세요.
- [x] 노선추가 & 구간 추가 시 거리와 함께 소요시간 정보도 추가하세요.
- [x] 인수 테스트 (수정) -> 문서화 -> 기능 구현 순으로 진행하세요.
- [x] 개발 흐름을 파악할 수 있도록 커밋을 작은 단위로 나누어 구현해보세요.

##### 요구사항 설명

- 인수 조건

```
Feature: 지하철 경로 검색
Scenario: 두 역의 최소 시간 경로를 조회
    Given 지하철역이 등록되어있음
    And 지하철 노선이 등록되어있음
    And 지하철 노선에 지하철역이 등록되어있음
    When 출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청
    Then 최소 시간 기준 경로를 응답
    And 총 거리와 소요 시간을 함께 응답함
```

- 요청 api 수정
    - source: 출발역 id
    - target: 도착역 id
    - type: 거리 or 시간

```Request
HTTP/1.1 200 
Request method:	GET
Request URI: http://localhost:55494/paths?source=1&target=3&type=DURATION
Headers: Accept=application/json
         Content-Type=application/json; charset=UTF-8
```

- 소요 시간 추가
    - 경로 조회 시 총 소요 시간을 조회하기 위해서는 노선과 구간을 생성할 때 소요 시간 정보를 함께 보내야 합니다.

```LineRequest
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int duration;
```