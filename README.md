# 핏밀리 - 가족과 함께 운동하며 건강을 나누는 소통형 헬스케어 앱

> **Fit**ness + Fa**mily** -> **Fitmily**

> **핏밀리(Fitmily)** 는 가족 간 건강한 소통을 중심으로 한 웰니스 앱입니다.<br/>
멀리 떨어져 있어도, 함께 살고 있어도 서로의 건강을 응원하며
산책 위치 공유, 운동 목표 달성률 확인, 가족 챌린지 등을 통해
**"가족 모두가 함께 건강해지는 경험"** 을 제공합니다.


## 🗂️ 목차
- [💪 핏밀리 서비스 소개](#-핏밀리-서비스-소개)<br/>
- [⏰ 개발 기간](#-개발-기간)<br/>
- [💡 기획 배경](#-기획-배경)<br/>
- [🎯 목표 및 주요 기능](#-목표-및-주요-기능)<br/>
- [🔧 기능 소개](#-기능-소개)<br/>
- [📢 기술 스택 소개](#-기술-스택-소개)<br/>
- [🔍 시스템 아키텍처](#-시스템-아키텍처)<br/>
- [💾 ERD 다이어그램](#-erd-다이어그램)<br/>
- [👥 팀 소개 및 역할](#-팀-소개-및-역할)<br/>

🔗 ~~핏밀리 바로가기~~ -> 서비스 종료<br/>
📝 [회의록 보기](https)  <br/>

## ⏰ 개발 기간
2025.04.14~2025.05.11 (4주) 기획, 설계<br/>
2025.05.12~2025.05.22 (2주) 개발<br/>

## 💡 기획 배경
현대 사회에서는 가족 간의 물리적 거리뿐만 아니라 심리적 거리도 점점 멀어지고 있습니다.
부모님의 건강이 걱정되지만, 실제로 어떤 운동을 하고 계신지, 얼마나 활동적인지 알기 어렵습니다.

“아빠는 오늘 운동을 하셨을까?”<br/>
“엄마는 요즘 어떤 동네를 산책하실까?”

핏밀리는 이러한 궁금증과 걱정을 해소하고, 가족 간의 대화를 건강이라는 주제로 자연스럽게 이어주는 플랫폼입니다.
함께 걷고, 함께 응원하며, 함께 기록하세요.<br/>

## 🎯 목표 및 주요 기능

#### 1. 가족 간 운동 & 산책 데이터 공유
- 실시간 **산책 위치 공유**
- 운동 현황 및 목표 진행률 **피트니스 링 시각화**
- **캘린더 기반 달성률** 표시로 가족 전체 운동 상황 한눈에 확인

#### 2. 소통 중심의 헬스 게이미피케이션
- **가족 산책 챌린지** 생성 및 달성
- **단체 채팅방**을 통한 실시간 소통
- **콕 찌르기** 기능으로 가족 간 운동 독려 및 응원<br/>

## 🔧 기능 소개
서비스의 주요 기능들을 소개합니다.
### 👤 회원 기능
#### ✅ 회원가입
- 이메일 인증, ID 중복 확인 하여 회원가입<br/>

#### ✅ 로그인 / 로그아웃
- 기본 폼 인증으로 로그인 / 로그아웃<br/>

### 🏠 홈 화면
#### ✅ 패밀리 그룹 생성
- 패밀리의 건강상태를 한 눈에 대시보드로 확인 가능<br/>

#### ✅ 산책 챌린지
- 가족 챌린지 달성 순위 등 확인 가능<br/>

#### ✅ 현재 날씨 확인
- 현재 위치의 날씨 확인 가능<br/>

### 👨‍👩‍👧‍👦 패밀리 기능
#### ✅ 패밀리 그룹 생성
- 패밀리 그룹을 생성<br/>

#### ✅ 패밀리 초대 및 가입
- 패밀리장은 그룹코드로 패밀리를 초대<br/>

### 🏃‍♂️ 나의 운동 데이터
#### ✅ 개인 건강 정보 등록
- 마이페이지에서 나의 건강상태 등록<br/>

#### ✅ 개인 운동 목표 등록
- 마이페이지에서 나의 운동 목표 등록<br/>

#### ✅ 개인 운동 기록 등록
- 마이페이지에서 나의 운동 기록 등록<br/>

#### ✅ 개인 운동 기록 현황
마이페이지에서 나의 운동 기록 현황 확인 가능<br/>

### 📊 가족 운동 캘린더
#### ✅ 가족 운동 기록 캘린더 시각화
- 가족 운동 기록을 캘린더로 시각화하여 보여줌<br/>

### 🌿 산책 기능
#### ✅ 실시간 산책 공유
- 실시간 산책 하고 있는 패밀리 위치 공유<br/>

#### ✅ 산책 기록 관리
- 패밀리가 종료한 산책 루트와 거리, 시간 등 상세 확인 가능<br/>

#### ✅ 주간 산책 챌린지
- 패밀리의 주간 목표 산책 거리 생성 후 가족이 해당 거리를 같이 산책<br/>

### 📣 가족 소통 기능
#### ✅ 단체 채팅
- 건강을 주제로 가족 간 채팅 가능<br/>

#### ✅ 콕 찌르기 기능
- "김아빠"님이 "김엄마"님에게 산책을 장려했어요!"<br/>



## 📢 기술 스택 소개

### ⚙️ Tech Stack

#### 📱 Android
<img src="https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white">
<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white">
<img src="https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white">
<img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white">
<img src="https://img.shields.io/badge/Google%20Maps-4285F4?style=for-the-badge&logo=googlemaps&logoColor=white">

#### 🔧 Backend
<img src="https://img.shields.io/badge/java17-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Cloud%20AWS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white">
<img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white">
<img src="https://img.shields.io/badge/socket.io-010101?style=for-the-badge&logo=socket.io&logoColor=white">
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black">

#### 🏗️ Build & Deployment
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white"> 
<img src="https://img.shields.io/badge/jenkins-%D24939.svg?style=for-the-badge&logo=jenkins&logoColor=white">

#### 🗄️ Database & Cache
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/MyBatis-DC382D?style=for-the-badge&logo=mybatis&logoColor=white">
<img src="https://img.shields.io/badge/mongoDB-47A248?style=for-the-badge&logo=MongoDB&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white">

#### ☁️ Infrastructure & Cloud
<img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
<img src="https://img.shields.io/badge/Amazon%20S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
<img src="https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white">
<img src="https://img.shields.io/badge/nginx-%23009639.svg?style=for-the-badge&logo=nginx&logoColor=white">
<img src="https://img.shields.io/badge/Let's%20Encrypt-003A70?style=for-the-badge&logo=letsencrypt&logoColor=white">

#### 📡 External Services & APIs
<img src="https://img.shields.io/badge/firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=white">
<img src="https://img.shields.io/badge/FCM-FFCA28?style=for-the-badge&logo=firebase&logoColor=white">

#### 🔍 Monitoring & Tools
<img src="https://img.shields.io/badge/GitLab-FCA326?style=for-the-badge&logo=gitlab&logoColor=white">
<img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white">
<img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white">


<br/>

## 🔍 시스템 아키텍처
<div align="center">
    <img src="/uploads/20e86e4df7eeb856d05aa2211bf06617/architecture.png" alt="아키텍처" width=80%>
</div>

<br/>

## 💾 ERD 다이어그램
<div align="center">
<img src="/uploads/8c8669cb71d7b3c72f372ab1fc149134/ERD.png" alt="ERD" width=80%>
</div>

<br/>

## 👥 팀 소개 및 역할

| Android | Android | Android | Backend | Backend | Backend, CI/CD |
|----------|----------|----------|----------|----------|----------|
| [수정](https://github.com/sujungeee) | [성현](https://github.com/hyuun) | [수미](https://github.com/LimSumi) | [동욱](https://github.com/okukay) | [예지](https://github.com/ygjeong5) (팀장) | [용성](https://github.com/omgomg333) |

<br>

## 👥 팀원 별 역할

### Android Developer

#### 🧑‍💻 수정 - 안드로이드 팀장
- 

#### 🧑‍💻 성현
- Figma를 통한 프로토타이핑 구현
- 월간 캘린더 / 주간 캘린더 / 상세 운동 구현
- 마이페이지 구현
- 목표 기능 구현
- 운동 기능 구현
- 건강 기능 구현

#### 🧑‍💻 수미
- UI 디자인
- 가족 대시보드 기능 구현
- 챌린지 기능 구현
- 가족 프로필 기능 구현
- 산책 기능 구현
    - stomp 활용
    - 실시간 가족 산책 경로 표시
    - fcm 알림 기반 UI 업데이트
    - 산책 기록 기능 구현

### Backend Developer

#### 🧑‍💻 동욱 - 백엔드 팀장
- 

#### 🧑‍💻 예지
- 가족 단체 채팅 시스템 구현 (MongoDB + Redis 기반 실시간 메시징)
- 패밀리 그룹 채팅방 관리 및 메시지 저장소 설계 (Redis 캐싱 활용)
- 주간 산책 챌린지 생성 및 관리 시스템 개발 (MyBatis 기반 데이터 처리)
- 챌린지 달성률 추적 및 가족 순위 시스템 개발
- FCM 푸시 알림 시스템 구현 (Firebase Cloud Messaging 연동)

#### 🧑‍💻 용성
- ERD 설계, API 명세
- Docker + Jenkins 를 통한 CI/CD pipeline 구축
- AWS EC2, S3 배포
- Nginx routing
- DB Admin
- Spring Boot 패밀리 기능 구현 (with MyBatis)


