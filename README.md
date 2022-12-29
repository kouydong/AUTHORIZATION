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
| 순번  |  프로세스   |     설명      |
|:---:|:-------:|:-----------:|
|  1  |  ADMIN  |  모든 접근 권한   |
|  2  | MANAGER |  사용자 접근 권한  |
|  3  |  USER   | 클라이언트 접근 권한 |  
※ 기타 권한을 추가하여 리소스 서버에 접근 권한을 제한 합니다.


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



