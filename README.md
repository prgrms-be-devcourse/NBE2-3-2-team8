![signal buddy](https://github.com/user-attachments/assets/2b4058df-117a-432c-9097-d09532db80ce)
[](https://youtu.be/FLnJ7vnxOQU?si=nGkfGYEmvXBuwhUr)

<p align="center"> 🚦<b>시그널 버디</b> 보행자 신호등 안내 서비스🚦</p>

---

## 팀원 소개
| BE | BE | BE | BE | BE |
| --- | --- | --- | --- | --- |
| <img src="https://avatars.githubusercontent.com/u/103233073?v=4" width=400px alt="오병일"/> | <img src="https://avatars.githubusercontent.com/u/82895809?v=4" width=400px alt="김동진"> | <img src="https://avatars.githubusercontent.com/u/108854865?v=4" width=400px alt="이동민"> | <img src="https://avatars.githubusercontent.com/u/104908845?v=4" width=400px alt="임서현"> | <img src="https://avatars.githubusercontent.com/u/145355985?v=4" width=400px alt="최주하"> |
| [오병일](https://github.com/ByungilOh-Fillip) | [김동진](https://github.com/Dongjin0224) | [이동민](https://github.com/DongminL) | [임서현](https://github.com/limseohyeon) | [최주하](https://github.com/zzuharchive) |
| 팀장 | 팀원 | 팀원 | 팀원 | 팀원 |

---
## 목표와 기능
<img src="https://github.com/user-attachments/assets/3ea03894-65b5-4e67-87e1-d4ace34199a9" align="left" width="200px">

🤢**스몸비란?**

📱**스마트폰**과 🤢**좀비**의 합성어로 스마트폰을 하느라 **좀비처럼 느리게 걸어가는 보행자**를 의미합니다.


🚦**시그널 버디(Signal Buddy)** 는

🚶‍♀️사용자가 **안전**하게 이동할 수 있도록
실시간 **신호등 점등 정보**를🚦 제공하는 시스템 입니다.  

---
## 기능

### **👥 사용자**

### [로그인/회원 가입]

- 일반적인 로그인/회원가입 기능과 소셜 로그인/회원가입 기능을 제공합니다.

### [메인]
- 사용자가 이동 경로를 검색하고, 경로 상에 위치한 신호등의 실시간 점등 정보를 제공합니다.
- 사용자가 지정한 위치의 신호등의 실시간 점등 정보를 확인할 수 있습니다.

### [마이페이지]

- 계정 관리 : 사용자 개인정보를 수정하고 관리할 수 있습니다.
- 자주 가는 곳 :  사용자가 자주 가는 위치를 등록하고 관리할 수 있습니다.
- 피드백 : 사용자가 작성한 피드백 목록을 조회할 수 있습니다.

### [사용자 피드백]

- 서비스에 대한 개선 사항과 신호등의 이상 상태를 관리자에게 알릴 수 있습니다.

### **🚦 관리자**

### [메인]

- 서비스에 등록되어 있는 신호등, 회원 관리, 피드백에 대해 요약된 관리 현황을 조회할 수 있습니다.

### [회원 관리]

- 회원에 대한 정보를 리스트로 조회할 수 있습니다.
- 회원에 대한 상세정보를 조회할 수 있습니다.

### [피드백 관리]

- 사용자가 작성한 글을 조회할 수 있습니다.
- 사용자의 피드백에 답변을 작성할 수 있습니다.

---
## 개발 환경&개발 도구

![6](https://github.com/user-attachments/assets/426e47cf-ee1a-41a8-bc0f-c7b3a6770a1b)


<aside>

- I**DE** : IntelliJ IEDA
- **DB** : MariaDB 11.5.2, Redis 7.4.1
- **Backend** : Spring Boot 3.4.0, Java 17
- **Data Access** : JPA, QueryDSL, JDBC
- **Authentication** & **Authorization** : Spring Security, Session
- **DevOps** : Docker, GitHub Actions
- **Communication** : Notion, Slack, ZEP
- **Build Tool** : Gradle
- **Mapping** : MapStruct 1.6.3
- **Template Engine** : Thymeleaf
- **Test** : JUnit5, TestContainers
</aside>

---
## 프로젝트 아키텍처
![7](https://github.com/user-attachments/assets/907b7b3f-db91-4fac-aa2a-35bd85cb3755)


---
## 요구사항 명세와 기능 명세
<img src="https://github.com/user-attachments/assets/b4ba7f97-7b31-47fd-be90-9e2f11c75220" align="left" width="50%">
<img src="https://github.com/user-attachments/assets/fe9532e6-1d40-44bd-abe0-cc33671f3385" align="left" width="50%">
