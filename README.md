# KotlinProjectAPI


## API 테스트 방법

### 1) 장소 검색
```aidl
// input
curl --location --request GET 'http://localhost:8080/search' \
--data-raw '{"title": "피자"}'

// output
[이재모피자, 파이프그라운드, 부자피자 1호점, 보니스피자펍, 뉴오더클럽, 피자덕후 피자힙 압구정점, 핏제리아오 대학로 본점, 도우개러지 피자 전포점]
```

### 2) 검색 키워드 목록
```aidl
// input
curl --location --request GET 'http://localhost:8080/getCount' \
--data-raw '{"title": "족발"}'

// output
[족발=2, 피자=1]
```

## 코드 설명
### APIController

fun search(@RequestBody param: Map<String, String>)
- 장소 검색

fun getCount()
- 검색 키워드 목록

### SearchAPI
- 장소 검색 API를 보내기 위한 구현
- KakaoSearchAPI, NaverSearchAPI

### SearchResult
- 장소 검색 API로 받아온 결과물을 제시한 요구에 맞게 정렬하여 출력

### countRepository
- 검색 키워드 목록을 저장하기 위해서 저장소를 구현
- H2 DB를 이용한 H2CountRepository , InMemory를 이용한 InMemoryCountRepository
- 둘 모두 구현했지만 현재는 H2 사용 (InMemory가 무한으로 늘어날 가능성으로 oom 이슈가 있을 수도 있어서 h2를 사용)


## 기술적 요구사항
### 지속적 유지 보수 및 확장에 용이한 아키텍처
- KakaoSearchAPI, NaverSearchAPI 는 SearchAPI 상속 받아 GoogleSearchAPI 등 추가 및 확장 가능
- H2CountRepository, InMemoryCountRepository 는 CountRepository 상속 받아 최소한의 코드 변경으로 변경 가능

### 동시성 이슈
- spring webflux 사용으로 적은 리소스로 동시성을 다룰 수 있음
- defer() 함수를 사용하여 구독이 이루워질 때까지 코드 블록을 lazy 하게 처리 할 수 있도록 구현

### 대용량 트래픽 처리
- spring webflux 사용으로 mvc보다 적은 수의 리소스로 많은 처리를 가능하게 함

