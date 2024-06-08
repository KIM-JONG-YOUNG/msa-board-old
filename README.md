# MSA Board
Spring Boot 및 React를 이용한 Microservice 게시판 

## Project Environments
- Java 1.8
- Maven 3.8.5
- Spring Boot 2.7.18
  - Spring Boot JPA
  - Spring Boot Redis
  - Spring Boot Kafka
  - Spring Boot Validation
  - Spring Boot WEB
  - Spring Boot Actuator
  - Spring Boot Security
- Spring Cloud 2021.0.9
  - Spring Cloud Open Feign
  - Spring Cloud Config Server
  - Spring Cloud Config Client
  - Spring Cloud Eureka Server
  - Spring Cloud Eureka Client
- Spring DOC Open API UI 1.7.0
- Mapstruct 1.5.2.Final
- JWT 0.9.1
- QueryDSL 4.4.0 
- MariaDB Client 2.7.9
- Yarn 1.22.19
- React 5.0.1
  - React Bootstrap
  - React Router Dom
  - React Hook Form
  - React Datepicker
  - React Error Boundary
- Docker 24.0.6
- Docker Compose 2.21.0

## Project Structure
```
msa-board
├── msa-board-clients                   
│   ├── msa-board-client-member        : 회원 마이크로서비스 요청 및 응답 DTO, Feign Client, REST API 스펙
│   ├── msa-board-client-post          : 게시글 마이크로서비스 요청 및 응답 DTO, Feign Client, REST API 스펙
│   └── msa-board-client-search        : 검색 마이크로서비스 요청 및 응답 DTO, Feign Client, REST API 스펙
├── msa-board-clouds                    
│   ├── msa-board-cloud-config         : 클라우드 설정 서비스 어플리케이션 (Spring Cloud Config Server)
│   └── msa-board-cloud-eureka         : 클라우드 Discovery 서비스 어플리케이션 (Spring Cloud Eureka Server)
├── msa-board-common                   : 게시판 공통 상수 및 Enum 
├── msa-board-configs                  : 게시판 설정 파일 
├── msa-board-cores                     
│   ├── msa-board-core-feign           : Feign 설정 및 내부 모듈 
│   ├── msa-board-core-kafka           : Kafka 설정 및 내부 모듈 
│   ├── msa-board-core-persist         : DB 설정 및 내부 모듈 
│   ├── msa-board-core-redis           : Redis 설정 및 내부 모듈 
│   ├── msa-board-core-security        : 보안 설정 및 내부 모듈 
│   ├── msa-board-core-transaction     : 분산 트랙잭션 설정 및 내부 모듈 
│   ├── msa-board-core-validation      : 유효성 체크 내부 모듈 
│   └── msa-board-core-web             : WEB 설정 및 공통 모듈 
├── msa-board-dockers                   
│   ├── msa-board-docker-cloud         : 클라우드 서비스 Docker Compose  
│   ├── msa-board-docker-endpoint      : 엔드포인트 서비스 Docker Compose
│   ├── msa-board-docker-kafka         : Kafka Cluster Docker Compose 
│   ├── msa-board-docker-mariadb       : MariaDB Docker Compose
│   ├── msa-board-docker-microservice  : 마이크로 서비스 Docker Compose
│   └── msa-board-docker-redis         : Redis Cluster Docker Compose
├── msa-board-domains                   
│   ├── msa-board-domain-member        : 회원 도메인 모듈 
│   └── msa-board-domain-post          : 게시글 도메인 모듈 
├── msa-board-endpoints                
│   ├── msa-board-endpoint-admin       : 게시판 관리자 엔드포인트 서비스 
│   └── msa-board-endpoint-user        : 게시판 일반회원 엔드포인트 서비스 
├── msa-board-microservices
│   ├── msa-board-microservice-member  : 회원 마이크로 서비스 
│   ├── msa-board-microservice-post    : 게시글 마이크로 서비스
│   └── msa-board-microservice-search  : 검색 마이크로 서비스
└── msa-board-views
    ├── msa-board-view-admin           : 관리자 화면 
    ├── msa-board-view-common          : 화면 공통 모듈 
    └── msa-board-view-user            : 일반회원 화면 

```

## Project Architecture

![image](https://github.com/KIM-JONG-YOUNG/msa-board/assets/46014387/1b5bfc56-cf2b-4249-a197-0d006987c262)
