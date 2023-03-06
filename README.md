# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 실습 - 문서화

### 요구사항
- PathDocumentation의 path 메서드를 완성시키세요.
- PathDocumentation의 테스트를 수행시켜 snippet을 생성하세요.
- gradle로 asciidoctor task를 수행시켜 문서 파일을 생성하세요
- build > asciidoc > html5 > index.html을 브라우저로 열고 캡쳐한 이미지를 PR에 포함시켜 주세요.
- PathSteps 클래스를 활용하여 PathDocumentation 내 코드 중복을 제거해주세요

## 🚀 1단계 - 경로 조회 타입 추가

### 요구사항

- 경로 조회 시 최소 시간 기준으로 조회할 수 있도록 기능을 추가하세요.
- 노선추가 & 구간 추가 시 거리와 함께 소요시간 정보도 추가하세요.
- 인수 테스트 (수정) -> 문서화 -> 기능 구현 순으로 진행하세요.
- 개발 흐름을 파악할 수 있도록 커밋을 작은 단위로 나누어 구현해보세요.