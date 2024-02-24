# 🚀 1단계 - 경로 조회 타입 추가

## 요구사항

## 기능 요구사항 - 최소 시간 경로 타입 추가
- [ ] 경로 조회 시 최소 시간 기준으로 조회할 수 있도록 기능을 추가하세요.
- [ ] 노선추가 & 구간 추가 시 거리와 함께 소요시간 정보도 추가하세요.
- [ ] 인수 테스트 (수정) -> 문서화 -> 기능 구현 순으로 진행하세요.
- [ ] 개발 흐름을 파악할 수 있도록 커밋을 작은 단위로 나누어 구현해보세요.

## 프로그래밍 요구사항
- 인수 테스트 주도 개발 프로세스에 맞춰서 기능을 구현하세요.
  - 요구사항 설명을 참고하여 인수 조건을 정의 
  - 인수 조건을 검증하는 인수 테스트 작성
  - 인수 테스트를 충족하는 기능 구현
- 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성하세요.
  - 뼈대 코드의 인수 테스트를 참고
- **인수 테스트 이후 기능 구현은 TDD로 진행하세요.**
  - 도메인 레이어 테스트는 필수
  - 서비스 레이어 테스트는 선택

## 요구사항 설명

### 인수 조건

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

### 요청 api 수정
- source: 출발역 id
- target: 도착역 id
- type: 거리 or 시간

```
HTTP/1.1 200
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=3&type=DURATION
Headers: 	Accept=application/json
Content-Type=application/json; charset=UTF-8
```

### 소요 시간 추가
- 경로 조회 시 총 소요 시간을 조회하기 위해서는 노선과 구간을 생성할 때 소요 시간 정보를 함께 보내야 합니다.
```
public class LineRequest {
private String name;
private String color;
private Long upStationId;
private Long downStationId;
private int distance;
private int duration;

    ...
```