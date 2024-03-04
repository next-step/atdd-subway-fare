# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션
---
# 🚀 실습 - Cucumber 전환

## 요구사항
- [ ] 경로 조회 인수 테스트 cucumber 전환하기
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
