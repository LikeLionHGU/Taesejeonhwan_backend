# 🎬 Otte  
### 취향 유사도를 통한 다각적 OTT 작품 탐색 소셜 플랫폼 (Backend)

> **Otte**는 넘쳐나는 콘텐츠 속에서 방황하는 시청자들을 위해  
> **사용자의 취향 데이터를 기반으로 OTT 작품을 탐색하고 발견하도록 돕는 소셜 큐레이션 플랫폼**입니다.

단순한 평점 추천을 넘어,  
사용자의 **취향 유사도**를 기반으로 콘텐츠를 탐색하며  

- 🎯 **안정적인 재미**
- 🌱 **새로운 발견**

을 모두 경험할 수 있도록 설계되었습니다.

---

# 📌 Overview

오늘날 OTT 서비스에는 수많은 콘텐츠가 존재합니다.  
하지만 많은 사용자는 다음과 같은 문제를 겪습니다.

- 무엇을 볼지 결정하기 어렵다
- 추천 알고리즘이 항상 정확하지 않다
- 새로운 작품을 발견하기 어렵다

**Otte는 이러한 문제를 해결하기 위해**

👉 **유저 취향 기반 탐색 시스템**  
👉 **취향 유사도 기반 소셜 추천**

을 제공합니다.

---

# ✨ 주요 기능

## 🔄 Switch Mode

사용자의 상황과 기분에 따라 **두 가지 탐색 모드**를 제공합니다.

| Mode | 설명 |
|-----|-----|
| **Similar Mode** | 나와 취향이 비슷한 유저의 작품 추천 |
| **Different Mode** | 나와 취향이 다른 유저의 작품 추천 |

이를 통해

- ✔ 안정적인 추천  
- ✔ 새로운 콘텐츠 발견  

두 가지 경험을 모두 제공합니다.

---

## 👥 User-centered Recommendation

기존 OTT 서비스와 달리 **작품 중심이 아닌 유저 중심 UI**를 제공합니다.

기존 방식
작품 → 평점 → 추천

Otte 방식
취향이 비슷한 유저 → 그 유저의 리뷰 → 작품 발견


즉,  
**신뢰할 수 있는 취향을 가진 유저를 통해 콘텐츠를 발견합니다.**

---

# 🛠 Tech Stack

### Backend
- Spring Boot 3.5.4
- Java 17+

### Database
- MySQL

### Storage
- AWS S3

### Authentication
- JWT
- CSRF Token

### Infrastructure
- AWS

---

# 🚀 Getting Started

## 1️⃣ Clone Repository

```bash
git clone https://github.com/LikeLionHGU/Taesejeonhwan_backend.git
cd Taesejeonhwan_backend

# Server
server.port=8443

# Database
spring.datasource.url=jdbc:mysql://43.201.11.179:3306/taesae
spring.datasource.username=Otte
spring.datasource.password=secret(^^b)
spring.jpa.hibernate.ddl-auto=update

# AWS S3
s3.bucket=fish-back
s3.region=ap-northeast-2
s3.access-key=****
s3.secret-key=****

./gradlew build
run = java -jar build/libs/Taesejeanhwan_backend-0.0.1-SNAPSHOT.jar
