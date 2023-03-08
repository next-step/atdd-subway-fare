# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션
## 🚀 실습 - 문서화
### 요구사항
- [x] 경로 찾기 기능을 문서화하기 위한 테스트를 작성하세요. 
  1. PathDocumentation의 path 메서드를 완성시키세요.
  2. PathDocumentation의 테스트를 수행시켜 snippet을 생성하세요. 
  3. gradle로 asciidoctor task를 수행시켜 문서 파일을 생성하세요.
  4. build > asciidoc > html5 > index.html을 브라우저로 열고 캡쳐한 이미지를 PR에 포함시켜 주세요. 
  5. PathSteps 클래스를 활용하여 PathDocumentation 내 코드 중복을 제거해주세요.
- [x] 테스트 작성 후 Spring Rest Docs 적용을 통해 문서에 기재할 정보를 설정하세요.
## 🚀 1단계 - 경로 조회 타입 추가
### 요구사항
- [ ] 경로 조회 시 최소 시간 기준으로 조회할 수 있도록 기능을 추가하세요.
- [x] 노선추가 & 구간 추가 시 거리와 함께 소요시간 정보도 추가하세요.
  - [x] 노선 등록 시, 소요 시간 정보를 보내도록 수정
  - [x] 구간 등록 시, 소요 시간 정보를 보내도록 수정
    - [x] 역과 역 사이에 구간을 등록할 시, 기존 구간의 소요 시간도 함께 변경해야함.
      ex) 강남역 - 선릉역 (소요시간 8분), 새로운 구간 강남역 - 역삼역 (소요시간 3분) -> 강남역 - 역삼역 (소요시간 3분), 역삼역 - 선릉역 (소요시간 5분)
- 구현 순서
  1. 노선 등록 인수 테스트에서 소요 시간 정보를 보내도록 수정
  2. 노선 등록 API 에 대한 문서 작성
  3. 노선 등록 시, 소요 시간 정보를 보내도록 서비스 테스트 코드 수정 및 개발
  4. 노선 등록 API 수정
  5. 구간 등록 인수 테스트에서 소요 시간 정보를 보내도록 수정
  6. 구간 등록 API 에 대한 문서 작성
  7. 구간 등록 시, 소요 시간 정보를 보내도록 서비스 테스트 코드 수정 및 개발
  8. 구간 등록 API 수정
  9. 경로 조회 시, 최소 시간 기준으로 조회할 수 있는 인수 테스트 코드 작성
  10. 최소 시간 기준으로 경로를 조회하는 단위 테스트 코드 작성
  11. 최소 시간 기준으로 경로 조회할 수 있는 API 작성