# 🤝 실시간 협업 플랫폼

**Spring Boot 기반의 실시간 협업 웹 플랫폼입니다.**

> 실시간 화면 공유, 팀 문서 협업, UML 다이어그램 작성, 작업 일정(WBS) 조율 기능을 통합 제공하여  
> 현대 협업 환경에서 필요한 핵심 기능들을 하나의 플랫폼으로 구현했습니다.

---

## 📅 프로젝트 정보

| 항목 | 내용 |
|------|------|
| **진행 기간** | 2024.11.11 ~ 2024.12.26 |
| **참여 인원** | 3명 |
| **담당 역할** | PM, 백엔드 개발 전체 |

---

## 🚀 기술 스택

| 구분           | 기술 |
|----------------|------|
| **Frontend**   | Thymeleaf, HTML, CSS, JavaScript |
| **Backend**    | Spring Boot, MyBatis, WebSocket |
| **Database**   | MariaDB |
| **Build Tool** | Maven |
| **WAS**        | Spring Boot 내장 Tomcat 9.0 |
| **Etc**        | Lombok, Git, Eclipse, HeidiSQL, Google Drive, SMTP(Naver) |

---


## 🌟 주요 기능

### 👥 사용자 & 팀 관리
- 회원가입/로그인
- 이메일 인증 기반의 팀 초대/수락 기능
- 초대장은 메일로 전송되며, 가입된 사용자만 초대 가능
- 초대 수락 시 팀 합류 → 채팅 및 협업 기능 사용 가능

### 💬 실시간 협업 기능
- 실시간 화면 공유 (마우스 커서 공유 포함)
- 팀 채팅 (WebSocket 기반)
- 실시간 UML / UI 다이어그램 작성
- 작업 일정 관리 (WBS 차트)

### 📧 이메일 기능
- 팀 초대장 전송 (SMTP 연동)
- 비밀번호 찾기 → 이메일 인증 + 토큰 기반 링크 처리

---

## 🚀 기술 스택

| 구분 | 기술 |
|------|------|
| **Backend** | Spring Boot, MyBatis, MariaDB, WebSocket |
| **Frontend** | Thymeleaf, HTML5, CSS3, JavaScript |
| **WAS** | Spring Boot 내장 Tomcat 9.0 |
| **개발 도구** | Eclipse, GitHub, HeidiSQL, Google Drive |
| **기타** | Lombok, SMTP(Naver메일 연동) |

---

## 🖥️ 화면 예시

### 🧾 회원가입  
![회원가입](https://github.com/user-attachments/assets/0c3a3391-a542-4d13-a18f-38197b0a77c6)
### 🔐 로그인  
![로그인](https://github.com/user-attachments/assets/2b821c78-ea7a-4354-add8-5cbda970997a)

### 📁 프로젝트 생성  
![프로젝트 생성](https://github.com/user-attachments/assets/ec9d58b1-9bd2-4e53-97b0-7c112c6d238d)

### 👥 팀원 추가  
![팀원 추가](https://github.com/user-attachments/assets/8277b8f0-5592-497a-9975-06a3f6a2fd81)

### 📧 프로젝트 추가 메일  
![프로젝트 추가 메일](https://github.com/user-attachments/assets/d6d7d746-7ae1-4969-9697-fa48e238787a)

### 📬 초대 메일 수락  
![초대 메일 수락](https://github.com/user-attachments/assets/23d71c1d-1551-4a96-9cee-6485769178a4)

### 💬 채팅 기능  
![채팅 기능](https://github.com/user-attachments/assets/54dea10d-8734-4267-9b97-e1b4bb7ab0a8)

### 🧩 UML 다이어그램 작성  
![UML 다이어그램 작성](https://github.com/user-attachments/assets/44ec21be-d1e3-41ba-9871-c764ef40c327)

### 📆 작업 일정(WBS) 관리  
![작업 일정(WBS) 관리](https://github.com/user-attachments/assets/1b428291-72a3-407d-be75-f04db5209968)

### 📤 이메일 발송 화면  
![이메일 발송 화면](https://github.com/user-attachments/assets/a611de73-982f-4ecc-bae1-d5f247eda189)

### 🔑 비밀번호 재설정  
![비밀번호 재설정](https://github.com/user-attachments/assets/713b18b9-992a-4214-b2ca-2f66393cb6b1)

---

## 🗂 시스템 구성도 및 DB 설계

- 시스템 아키텍처 및 DB ERD는 아래와 같습니다.


### 시스템 구성도
![system-architecture](https://github.com/user-attachments/assets/a35d1e90-746d-4d8a-9539-cee5573a2412)

### 데이터베이스 ERD
![erd](https://github.com/user-attachments/assets/6765588f-469e-4969-8c28-e31a9281e131)

---

## 📝 프로젝트 회고
이전까지 진행했던 프로젝트는 대부분 정적인 데이터를 다루는 구조였지만,  
이번 프로젝트에서는 **WebSocket**을 활용해 클라이언트와 서버 간 지속적인 연결을 유지하는 경험을 할 수 있었습니다.
또한, **비밀번호 재설정 기능**에서는 단순히 메일만 보내는 것이 아니라, **토큰**을 발급하고 유효 시간 내에만 비밀번호를 변경할 수 있도록 제한하는 인증 흐름으로 구현하였습니다.
이를 통해 보안 관점에서도 사용자 인증 시스템의 기초를 체험할 수 있었고, JWT나 OAuth 같은 고급 인증 방식으로 확장할 수 있는 기반을 스스로 만들어볼 수 있다는 점이 인상 깊었습니다. 
이번 프로젝트를 통해 단순한 CRUD 기반 개발을 넘어, **실시간성과 인증 흐름을 고려한 웹 서비스** 구조 설계에 한걸음 더 다가갈 수 있었습니다.

---
