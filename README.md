# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션
---
# 🚀 실습 - Cucumber 전환

## 요구사항
- [x] 경로 조회 인수 테스트 cucumber 전환하기
  - [x] features 작성 (DataTable / 파라미터 / 공유 객체 활용)
  - [x] cucumber 환경에서의 데이터 초기화 설정<br>
    `Before과 Background 모두 각 시나리오 전에 수행된다는 점은 같다.`
    - Before : 메서드 형식으로 사용하기 때문에 보다 명시적으로 보여주기 위해 Background 사용을 권장한다.
    - Background : 각 시나리오 마다 수행하지만 시점은 Before 이후이며 feature 파일에 작성한다.
```
PathAcceptanceTest의 경로 조회 인수 테스트를 cucumber 기반으로 작성합니다.
station.feature를 참고하여 진행하세요.
cucumber가 제공하는 기능을 적절히 활용해보세요.
```
---
# 🚀 1단계 - 경로 조회 타입 추가

## 요구사항
- [x] 최소 시간 경로 타입 추가(기존의 최단 거리 기준에서 추가된다)
  - [x] 경로 조회 시 최소 시간 기준으로 조회할 수 있도록 기능 추가
  - [x] 노선추가 & 구간 추가 시 `거리`와 함께 `소요시간` 정보 추가
  - [x] 작은 단위로 커밋 진행

## 프로그래밍 요구사항
- 인수 테스트 주도 개발 프로세스에 맞춰서 기능 구현
  - [x] 인수 조건 정의 > 인수 테스트 작성 > 기능 구현
- [x] 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성
---
## Feedback 24.03.10

- [x] 중복되는 사전조건(지하철 역들/노선들을 생성한다)은 background를 활용해도 좋을 것 같다.
  - Background 설정
  - AcceptanceContext에서 station객체와 line 객체를 각각 관리 
- [ ] 패키지 구조의 게층이 잘 그려지지 않는 문제. subway 하위에 도메인으로 나뉠지, 계층별로 나뉠지 통일이 필요해 보임
- [ ] PathFinder 객체의 이름이 PathFinder 보다는 Path나 SubwayMap과 같이 바라볼 수 있을 것 같음. 고민 필요
- [ ] 검증 시 resopnse.jsonPath().getList("...")와 같은 부분의 각 단계를 메서드로 분리하면 재사용도 가능하고 시나리오가 더욱 명확해질 수 있다.
- [ ] 거리의 타입이 int 대신 long 인 이유는?
  - 표현할 수 있는 값의 범위가 큰 것이 무조건적인 장점일까? 저장할 데이터의 최소/최대 범위를 고려해서 자료형을 정해야 한다.
