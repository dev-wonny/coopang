# 🍱 Coopang 🍱
물류 관리 및 배송 시스템을 위한 MSA, 스프링 부트 기반 프로젝트 입니다.
![스크린샷]()

## 🗓️ 개발 기간
* 2024.09.05 ~ 2024.09.19
* [Github Projects Todo 링크](https://github.com/users/dev-wonny/projects/1)
  
## 📚 목차
[1. 프로젝트 개요](#1-프로젝트-개요)

[2. 요구사항 명세서](#2-요구사항-명세서)

[3. 인프라 설계도](#3-인프라-설계도)

[4. 서버 설계](#4-서버-설계)

[5. API 명세서](#5-api-명세서)

[6. 테이블 설계서](#6-테이블-설계서)

[7. ERD](#7-erd)

[8. 기술 스택](#8-기술-스택)

[9. Git Branch 및 Git Commit 전략](#9-git-branch-및-git-commit-전략)

[10. 시연영상](#10-시연영상)

[11. 4L 회고](#11-4L-회고)

## ✅ 1. 프로젝트 개요
* **주제:** 물류 관리 및 배송 시스템 개발
* **목표:** 물류 관리 및 배송 시스템 개발

## 📕 2. 요구사항 명세서
* Wiki에 따로 요구사항 명세서를 기록하였습니다.
* [📘 Wiki - 요구사항 명세서 링크](https://antique-ease-afd.notion.site/7f70de7531674898b18a204d5bc824c6?pvs=4)

## 🛠️ 3. 인프라 설계도
![인프라설계도](./IMG/쿠팡.drawio.png)

## 🛠️ 4. 서버 설계
* 아래 Notion 링크를 통해서 확인 가능합니다.
* [Coopang - 서버 설계 - Notion](https://antique-ease-afd.notion.site/3a35eef966b34e08b1c455f537f92874?pvs=4)

## 📙 5. API 명세서
* API 명세서에 설계에 따라 개발을 하였고, 첫 설계와 다른점이 있다고 생각하면 [🛠️ [API 명세서] 수정사항 #8](https://github.com/dev-wonny/coopang/issues/8)에 기록하였습니다.
* 아래 Notion 링크를 통해서 확인 가능합니다.
* [Coopang - API 명세서 - Notion](https://antique-ease-afd.notion.site/1e40c3559a184e4cbd41dbbda31fe395?v=0f1972d04af54c1e800b767fa6f69ca1&pvs=4)

## 📄 6. 테이블 설계서
* 요구사항 명세서의 **데이터 베이스 설계**의 규칙에 따라 작성하였습니다.
* 아래 Notion 링크를 통해서 확인 가능합니다.
* [Coopang - 테이블 설계서 - Notion](https://antique-ease-afd.notion.site/down-grade-a8766e06208e4857b113aa6cfb114422?pvs=4)

## 📋 7. ERD
![ERD](./IMG/erd.png)
* 테이블 설계서에 따라 만든 ERD입니다.

## 🛠️ 8. 기술 스택
* Backend
    * Spring Boot 3.3.3
    * Spring Cloud Gateway
    * Gradle
    * JWT
    * Oauth2
    * QueryDSL
    * JPA
* API Test
    * Swagger
* Database
    * Postgresql
    * Redis
* Infra
    * Docker
* Version
    * Git
    * Github


## 📀 9. Git Branch 및 Git Commit 전략

* Git Commit 전략
    * 자세한 내용은 [📘 Wiki - GitCommit 전략 링크](https://github.com/dev-wonny/coopang/wiki/Commit-%EC%A0%84%EB%9E%B5) 에서 확인 가능합니다.

|머리말|내용|
|-----|-----|
|Init|시작|
|Fix|버그 수정|
|Add|새로운 기능 추가|
|Update|기존 기능 업데이트|
|Remove|불필요한 코드 제거|
|Refactor|코드 리팩토링|
|Improve|성능 개선|
|Document|문서화|
|Style|스타일 변경 (예: 코드 포맷팅)|
|Test|테스트 추가 또는 수정|

## 🎥 10. 시연영상
* [ ▶ 시연영상 YouTube 링크]()

## 🤓 11. 4L 회고
* 4L 이란??
  > Liked : 좋았던 것/잘 한 것, Lacked : 아쉬웠던 것/부족했던 것, Learned : 배운 것, Longed for : 바라는 것/개선을 위해 시도해볼 것을 작성해보면서 프로젝트를 회고하는 방법입니다.

<details>
    <summary><h2>4L</h2></summary>

### 1. Liked : 좋았던 것/잘 한 것

  
### 2. Lacked : 아쉬웠던 것/부족했던 것


### 3. Learned : 배운 것


### 4. Longed for : 바라는 것/개선을 위해 시도해볼 것

</details>
