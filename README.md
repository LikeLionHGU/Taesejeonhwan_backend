🔛 Otte- 취향 유사도를 통한 다각적 ott 작품 탐색 소셜 플랫폼 (Backend) ⚡

---

🔛 서비스 소개
Otte는 넘쳐나는 콘텐츠 속에서 방황하는 시청자들을 위한 유저 취향 기반 OTT 큐레이션 서비스입니다. 
단순한 평점 나열을 넘어, ‘안정적인 재미’ 와 ‘새로운 발견’을 오가는 스위치 모드를 통해 인생작을 만나는 시간을 선물합니다.

---

✨ 주요 기능
🔄스위치 모드: 유저의 기분에 따라 선택할 수 있는 두 가지 모드(비슷한 취향과 반대 취향) 탐색을 제공한다.
👍유저 중심 추천: 작품 포스터가 아닌, 취향이 검증된 유저의 프로필과 리뷰 리스트를 전면에 배치한다.

---

🛠️ 기술 스택
Framework: Spring Boot 3.5.4
Language: Java 17+
Database: MySQL
Storage: AWS S3
Auth: JWT, CSRF 토큰

---

🚀 시작하기
1) 프로젝트 클론
git clone https://github.com/LikeLionHGU/Taesejeonhwan_backend.git
cd Taesejeonhwan_backend


2) 환경 설정 (application.properties)
# Server
server.port=8443

# Database
spring.datasource.url=jdbc:mysql://43.201.11.179:3306/taesae
spring.datasource.username=Otte
spring.datasource.password=secret(^^b)
spring.jpa.hibernate.ddl-auto=update

# S3
s3.bucket=fish-back
s3.region=ap-northeast-2
s3.access-key=****
s3.secret-key=****

3) 빌드 및 실행
./gradlew build
java -jar Taesejeanhwan_backend-0.0.1-SNAPSHOT.jar
﻿
