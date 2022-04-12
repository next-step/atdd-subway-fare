# 요구사항 설명
## 인수 조건

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

## 소요 시간 추가

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
