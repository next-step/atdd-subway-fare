# NEXTSTEP 지하철 노선도 미션
> [ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션


## 미션 피드백 링크

- Step 0 : [https://github.com/next-step/atdd-subway-fare/pull/320](https://github.com/next-step/atdd-subway-fare/pull/320)
- Step 1 : [https://github.com/next-step/atdd-subway-fare/pull/331](https://github.com/next-step/atdd-subway-fare/pull/331)


## 미션 관련 ERD
- ERD Cloud - https://www.erdcloud.com/d/tquYyrSeEHaqpZThG

<img src="images/DB-ERD.png">

## 미션 내용

### STEP 0
- [x] 경로 조회 기능 문서화

### STEP 1
- [x] 구간 추가 시 소요시간 정보도 추가하도록 변경
- [x] 구간 삭제 시 소요시간 정보도 합쳐지도록 변경
- [x] 노선 추가 시 소요시간 정보도 추가하도록 변경
- [x] 경로 조회 타입 추가
  - [x] 인수 테스트 수정
  - [ ] 문서화
  - [ ] 기능 구현
    - [ ] 소요 시간으로 가중치를 측정하는 메서드 추가
    - [ ] 타입에 맞게 조회 메서드를 사용하도록 변경
