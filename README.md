# KotlinProjectAPI


## API 테스트 방법

### 1) 장소 검색
```
// input
curl --location --request GET 'http://localhost:8080/search' \
--data-raw '{"title": "피자"}'

// output
[이재모피자, 파이프그라운드, 부자피자 1호점, 보니스피자펍, 뉴오더클럽, 피자덕후 피자힙 압구정점, 핏제리아오 대학로 본점, 도우개러지 피자 전포점]
```

### 2) 검색 키워드 목록
```
// input
curl --location --request GET 'http://localhost:8080/getCount' \
--data-raw '{"title": "족발"}'

// output
[은행=11, 양꼬치=8, 쌀국수=5, 피자=5, 떡볶이=5, 김밥=5, 치킨=4, 국밥=4, 양갈비=4, 삼겹살=4]
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
- 대용량 트래픽 처리를 위한 RedisCountRepository를 embedded redis를 이용해서 구현



## 기술적 요구사항
### 지속적 유지 보수 및 확장에 용이한 아키텍처 , 새로운 검색 API
- KakaoSearchAPI, NaverSearchAPI 는 SearchAPI 상속 받아 GoogleSearchAPI 등 추가 및 확장 가능
- H2CountRepository, InMemoryCountRepository 는 CountRepository 상속 받아 최소한의 코드 변경으로 변경 가능

### 동시성 이슈, 대용량 트래픽 처리
- spring webflux 사용으로 mvc보다 적은 수의 리소스로 많은 처리를 가능하게 함
- embedded redis로 대용량 트래픽 처리를 할 수 있게 처리, addCount 캐시
- defer() 함수를 사용하여 구독이 되기 전까지 코드 블록을 lazy 하게 처리 할 수 있도록 구현

