![Java_17](https://img.shields.io/badge/java-v17-red?logo=java)
![SPRING BOOT](https://img.shields.io/badge/SPRING_BOOT-v2.7.6-green.svg?logo=spring)
![WEB FLUX](https://img.shields.io/badge/WEB_FLUX-v2.7.6-green.svg?logo=spring)
![SPRING SECURITY](https://img.shields.io/badge/SPRING_SECURITY-v3.0.0-green.svg?logo=spring)
![JPA](https://img.shields.io/badge/JPA-green.svg?logo=spring)
![H2](https://img.shields.io/badge/H2-green.svg?logo=spring)
![JWT](https://img.shields.io/badge/JWT-green.svg?logo=spring)

# OPEN API 인증서버(Authorization Server) 구축
- 작성자 : 고의동
- Author : Kudd

## 개요(Overview)
- SPRING BOOT 에서 제공하는 SPRING SECURITY 및 JWT를 통한 인증 서버 구현
- Implementation of the Authentication Server which is used by Spring Security and JWT

## 환경(Environment)
- Java version : 17
- Spring-boot version 2.7.6
- DBS : H2, JPA
- In-Memory DB 사용 시 DB 접근 경로(http://localhost:9100/h2-console)

## 의존성(Dependencies)
- 모든 라이브러리는 MavenCentral 에서 다운로드 가능
- libraries are able for downloading in Maven Central Repository

## 역할 및 권한(Role & Authority)
| 순번  |  프로세스  |     설명      |
|:---:|:------:|:-----------:|
|  1  | ADMIN  |  모든 접근 권한   |
|  2  |  USER  |  사용자 접근 권한  |
|  3  | CLIENT | 클라이언트 접근 권한 |  
※ 기타 권한을 추가하여 리소스 서버에 접근 권한을 제한 합니다.


## 프로세스 흐름도(Process Flow)
| 순번  |          프로세스           |                  설명                  |   담당   |      URI       |     Method Type     |
|:---:|:-----------------------:|:------------------------------------:|:------:|:--------------:|:-------------------:|
|  1  |       맴버(클라이언트)등록       |      OPEN API 사용하기 위한 클라이언트 등록       | 아파트 아이 | /members/join  |        POST         |
|  2  | 로그인(Access Token 생성 요청) |         클라이언트를 위한 엑세스 토큰 발급          | 클라이언트  | /members/login |        POST         |
|  3  |         API 호출          | 엑세스 토큰을 통해 인증서버 유효성 통과 후 API 호출하여 응답 | 클라이언트  |   /api/****    | GET/POST/PUT/DELETE | 


### 1. 맴버(클라이언트) 등록
- 맴버(클라이언트)를 등록하는 프로세스이며 아파트아이 담당자가 등록 한 후 클라이언트에게 전달합니다.


- 요청 예시 : curl -X POST localhost:9100/members/join -H "charset=utf-8" -d "{\"memberId\": \"kudd\", \"password\": \"1234\"}"


- 응답 예시 : 
  {
  "memberId": "kudd",
  "encryptedPassword": "{bcrypt}$2a$10$qP36rWUQOy/RhdK4VQh/h.jbf1IBKO/UQQqYRcDAh/HxqpJCqH6/G",
  "encryptedKey": "jbf1IBKO/UQQqYRcDAh/HxqpJCqH6/G"
  }

### 2. 로그인(Access Token 생성 요청)
- 아파트아이 담당자에게 제공 받은 memberId, encryptedPassword 를 통해 엑세스 토큰들 전달 받습니다.


- 요청 예시 : curl -X POST localhost:9100/members/join -H "charset=utf-8" -d "{\"memberId\": \"kudd\", \"password\": \"{bcrypt}$2a$10$qP36rWUQOy/RhdK4VQh/h.jbf1IBKO/UQQqYRcDAh/HxqpJCqH6/G\"}"


- 응답 예시 :
  {
  "grantType": "Bearer",
  "accessToken": "eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBUEktSSIsInN1YiI6IkFQVC1JIEFjY2VzcyBUb2tlbiIsInJvbGUiOiJST0xFX0FETUlOLFJPTEVfS09VWURPTkcsUk9MRV9VU0VSIiwianRpIjoia291eWRvbmciLCJhdWQiOiJrb3V5ZG9uZyIsImlhdCI6MTY3MTY5Nzg5MSwibmJmIjoxNjcxNjk3ODkxLCJleHAiOjE2NzE2OTgxOTF9.WE7A9hrXDxCHoFQ_SWGl2bD6p0n66fUf4Wsw0dF8M1k",
  "refreshToken": "eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBUEktSSIsInN1YiI6IkFQVC1JIFJlZnJlc2ggVG9rZW4iLCJqdGkiOiJrb3V5ZG9uZyIsImF1ZCI6ImtvdXlkb25nIiwiaWF0IjoxNjcxNjk3ODkxLCJuYmYiOjE2NzE2OTc4OTEsImV4cCI6MTY3MTY5ODE5MX0.S0Y7fo2Z9G6szwPu-z98FH4F1A3GgQFg5d1srTaAzDk",
  "roles": [
  "ADMIN",
  "KOUYDONG",
  "USER"
  ]
  }

### 3. API 호출
- 엑세스 토큰(수명주기 300일)을 통해서 API 호출을 통해 결과 값을 응답 받습니다.


- 요청 예시 : curl -X GET localhost:9100/api/apartments/users/29278/101/1104 -H "Authorization Bearer eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBUEktSSIsInN1YiI6IkFQVC1JIEFjY2VzcyBUb2tlbiIsInJvbGUiOiJST0xFX0FETUlOLFJPTEVfS09VWURPTkcsUk9MRV9VU0VSIiwianRpIjoia291eWRvbmciLCJhdWQiOiJrb3V5ZG9uZyIsImlhdCI6MTY3MTY5Nzg5MSwibmJmIjoxNjcxNjk3ODkxLCJleHAiOjE2NzE2OTgxOTF9.WE7A9hrXDxCHoFQ_SWGl2bD6p0n66fUf4Wsw0dF8M1k"


## 예외처리
- 스프링 시큐리티 Filter 통해서 예외 처리를 진행합니다

#### 토큰 예외처리 정보
| 순번  | 에러코드  |         ENUM 예외 상수          |       설명        |
|:---:|:-----:|:---------------------------:|:---------------:|
|  1  | 20701 |   TOKEN_INVALID_EXCEPTION   | 유효하지 않은 JWT 토큰  |
|  2  | 20702 |   TOKEN_EXPIRED_EXCEPTION   |   토큰 접속 시간 만기   |
|  3  | 20703 | TOKEN_UNSUPPORTED_EXCEPTION |  지원하지 않는 토큰 정보  |
|  4  | 20704 | TOKEN_CLAIM_EMPTY_EXCEPTION |  토큰 클레임 정보 없음   |
|  5  | 20705 | TOKEN_ROLE_EMPTY_EXCEPTION  | 토큰 역할(권한) 정보 없음 |



