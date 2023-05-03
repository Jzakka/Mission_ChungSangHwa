# 3Week_정상화.md

## Title: [3Week] 정상화

### 미션 요구사항 분석 & 체크리스트

---
> - [x] 네이버클라우드 배포
> - [x] 수정,삭제 쿨타임
> - [x] **[선택]** 알림기능 

### 3주차 미션 요약

---

**[접근 방법]**

체크리스트를 중심으로 각각의 기능을 구현하기 위해 어떤 생각을 했는지 정리합니다.

>### 수정, 삭제 쿨타임
> ***
> 1. modifyDate와 현재 시간의 차이가 3시간이내면 RsDate를 실패로 만든다.
> 2. 테스트에서는 modifyDate를 강제로 3시간 전으로 돌린다.
> 3. modifyDate를 애플리케이션에서 수정하려면 네이티브 쿼리를 날리는 방법 밖에 없음

>### 알림기능
> ***
> 1. 호감표시, 호감변경이 일어났을 때 이벤트리스너에 새로운 동작을 추가한다.
> 2. 새로운 동작은 NotificationService가 주체이다.
> 3. 알림이 뷰에서 보일 때는 `yy.MM.dd HH:mm ~시간 전` 의 형식으로 보여야 한다.
> 4. 날짜 포매팅은 자바스크립트로 브라우저가 처리하게 했고 이 때 사용된 라이브러리는 date-fns 1.x 구버전이다.

> ### 네이버클라우드 배포
> ***
> **빌드**
> - gradle은 의존성관리와 빌드를 도와줌
> - 네이버클라우드 머신에는 gradle이 없으므로 따로 받아야함
> - gradle은 maven 리포지토리에도 없다. 직접 zip파일 받고 압축 풀어야함
> - 그 외에도 SDKMAN을 이용하여 여러버전의 그레이들을 관리할 수도 있음
> - gradle을 설치할 때 주의점이 로컬의 gradle버전과 같거나 그 이상 최신버전을 선택해야함
> - 안 그러면 라이브러리 의존성에서 문제가 생길 수도 있다
> 
> **실행1 (클라우드에서 직접 실행)**
> - 빌드된 jar파일은 프로젝트의 build/libs 폴더에 생성됨
> - 해당 jar파일을 실행하면 스프링부트프로젝트가 실행된다.
> - 이 때에는 운영환경이므로 프로필을 prod로 변경해야한다.
> - `java -jar -Dspring.profiles.active=prod build/libs/gramgram-0.0.1-SNAPSHOT.jar`
> <br/> 위의 명령어의 `-Dspring.profiles.active`를 통해 yml파일에 정의해두었던 활성 프로필을 변경하여 실행가능하다.
> 
> **실행2 (도커 컨테이너로 실행)**
> - 프로젝트를 빌드하는 과정까진 이전과 동일
> - 운영 yml 파일의 변경점 추가
>  
>   1. 운영DB의 url 호스트가 172.17.0.1이어야 한다. 도커 컨테이너가 localhost가 아닌 클라우드(도커 호스트)의 3306으로 접속해야하기 때문
>   2. oauth2의 redirect-uri이 baseUrl을 `https://도메인/...` 으로 직접 수정해야한다. nginx가 통신을 https로 바꿔주긴 하지만 oath의 redirect-uri까지 변경해주진 않는다. 
> 
> **결과**
> ![결과이미지](https://lh3.googleusercontent.com/OsjWzyWi1qb5yPK-49l5HgaLZDPFQU1AABW4Xmumu_LHbtaUSJSvw2MeuuoAF9uMKHHJiYxAZ-aIWTCRRwPeLL2fHe-SP7Qcude_XyKVI_B3016G1SkS5R84ysmKFq8Eg_IA5lFiPkxw9dcAr_bX9lJNWwV6q0u8n191IvRZxZFuryL1fPOdCrm04b6BqV3E69lV8Wch4A4zg1tpVDX91tSsWA-EjmM0SBaWI-eMo6i0rAEEXSAQfTl9bG_XAUp6Wf-v7DujapRb5gCdGZB7a6BFxOS2dHV7bnCxyXOaHv1RBya1vHEQixVtKErwOJc_MW0T48iWxHeA_3dTMfsFVARijOUZx2uXVEkyZ_2GKDBjNbgz9jmb0YORiDa2sn-SH0yLy_J4Nogx81pQJCjMFEu8jN2FYyX5VmeliNfEsaGmqo2aLrPQTLWO73UApdT-qdBo-snSWgxc66aBa92QiL7h48F7Km4PNfIMIhQ7ihl8MwuT3Hm-dYsEF9behg-VfyDNFjVa2vBXnMgMyH1oqoV24wf1jvIXnykbp7f4wxBWyIUzKH-AOxePa0EN078pdHPcDdARcgvDBkk6gIQvqzQoeMraIF-5RRU_qW-jA8Y313IP_MTdZwNGBdZyWUnXOJkv0AKDkjpf2UQryVE7tMQXZLVhjfZeDiqTB8H976cj23GTok2w-Z4Gj20-3w-rdFbSbkYrzAOj5fNawZnrcTyyUSU9RecMWXFtmynSlnrPLlfXmj-ZzikB4zpM1_-UFjlAe0un6YAhk0Ki2lcLxo2TT0LOOYvBBQa69983qXFPP0tSdxAX0ai_XfLVeVR-WvE2n-ogrUExzjlsTNDJNdLRPS0dnITLznbHbp5UyU0FEopDk3Ad4ZZeQFZaP5KnymsvsQ837T13WMw0yFLaQmhLMVpWoHg3AEGSBaxK2ifG-ATuGwC2AHddJ948R4fBa20MzOfVjBuqaTUxDJQ=w1202-h1536-s-no?authuser=0)

**[특이사항]**

구현 과정에서 아쉬웠던 점 / 궁금했던 점을 정리합니다.

- <span style="color:gray">~~아직 nginx proxy manager가 동작하고 있지 않아서 http 연결로 동작한다. (그래서 OAuth와 같은 인증이 안된다.)~~</span>
- <span style="color:gray">~~프로젝트를 하나의 도커컨테이너 실행시키는 것도 좋은 방법일 것 같다.~~</span>
- gradlew로 실행시키는 방법과 jar를 java로 실행시키는 방법도 있던데 둘이 어떻게 차이가 있는걸까
- modifyDate와 현재시간을 비교하여 쿨타임을 재는 것이 간단하지만 테스트를 위해 리포지토리에 생짜쿼리를 날리는 메서드를 만들어야 하는 게 단점인 것 같다.
- 알림기능은 이벤트와 이벤트리스너를 이용하여 구현했다. 이벤트 리스너의 경우 엔티티별로 여러개를 만들 수가 있는걸까?
- 현재 InstaMemberEventListener가  InstaMember 엔티티의 로직을 처리하고 NotificationService를 통해 알림까지 발송한다.
- XXXEventListener가 엔티티마다가 존재해서 해당 엔티티에 관련된 로직만을 처리할 수 있는 걸까