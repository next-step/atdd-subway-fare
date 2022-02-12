<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```


# 📌 0단계 - 문서화 실습
- 경로 찾기 기능을 문서화하기 위한 테스트 작성 및 Spring Rest Docs 적용
- Path 문서화 테스트 중복 제거

# 🚀 1단계 - 경로 조회 타입 추가
- 경로 조회 시 최소 시간 기준으로 조회하는 기능 추가
- 노선 추가 및 구간 추가 시 소요시간 정보 추가

# 🚀  2단계 - 요금 조회
- 경로 조회 결과에 요금 정보를 포함