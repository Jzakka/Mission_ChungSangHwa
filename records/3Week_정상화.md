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

>**수정, 삭제 쿨타임**
> 1. modifyDate와 현재 시간의 차이가 3시간이내면 RsDate를 실패로 만든다.
> 2. 테스트에서는 modifyDate를 강제로 3시간 전으로 돌린다.
> 3. modifyDate를 애플리케이션에서 수정하려면 네이티브 쿼리를 날리는 방법 밖에 없음

>**알림기능**
> 1. 호감표시, 호감변경이 일어났을 때 이벤트리스너에 새로운 동작을 추가한다.
> 2. 새로운 동작은 NotificationService가 주체이다.
> 3. 알림이 뷰에서 보일 때는 `yy.MM.dd HH:mm ~시간 전` 의 형식으로 보여야 한다.
> 4. 날짜 포매팅은 자바스크립트로 브라우저가 처리하게 했고 이 때 사용된 라이브러리는 date-fns 1.x 구버전이다.

>**네이버클라우드 배포**
> - 클라우드에서 이 프로젝트를 실행시키면 됨
> - 클라우드에는 인텔리제이도 없고 실행버튼도 없는데 어떻게? <br/><span style="color:pink">CLI로 직접 빌드 후 실행</span>
> 
> **빌드**
> - gradle은 의존성관리와 빌드를 도와줌
> - 네이버클라우드 머신에는 gradle이 없으므로 따로 받아야함
> - gradle은 maven 리포지토리에도 없다. 직접 zip파일 받고 압축 풀어야함
> - 그 외에도 SDKMAN을 이용하여 여러버전의 그레이들을 관리할 수도 있음
> - gradle을 설치할 때 주의점이 로컬의 gradle버전과 같거나 그 이상 최신버전을 선택해야함
> - 안 그러면 라이브러리 의존성에서 문제가 생길 수도 있다
> 
> **실행**
> - 빌드된 jar파일은 프로젝트의 build/libs 폴더에 생성됨
> - 해당 jar파일을 실행하면 스프링부트프로젝트가 실행된다.
> - 이 때에는 운영환경이므로 프로필을 prod로 변경해야한다.
> - `java -jar -Dspring.profiles.active=prod build/libs/gramgram-0.0.1-SNAPSHOT.jar`
> <br/> 위의 명령어의 `-Dspring.profiles.active`를 통해 yml파일에 정의해두었던 활성 프로필을 변경하여 실행가능하다.
> 
> **결과**
> ![](https://lh3.googleusercontent.com/ggEpQ-xCkcK2FxQcGOhcXRP5NzQ6L91xWkdFxdAmJ7U42GIY2xGnt0acfO0fJvIG21LVmOnyBM0o7A1lNizyKHiMMZ12N1_m5jjV_KC66hBsJiVUQZ4c3pzRrg93UjCPUDPBJycmgHAuv1fKJObi15kd0mVFUPblIdEfXQ6ERjmzA3qG6kvLmrMHeUBDOIdiQEp6p7vyDuM1MMVezZPVNl6qfjs4vLED2606Po4DZM2c1teXUX1QHpdvdmfU7UYPK6i6G8IJVUF0H_49PJ-C4xaVOyuxDOPE4qkyIIiSecG_hwLh8_9vm4OIgiTo-lVehDXyIcD6EmwOr0o0_O6rJaOG_5xcvR4cbDrcPoEfFrQsmaoJ2qSJqGUpyMztTG7hyY275kOMx-OoknYBsKithWJtS67WA3djwGf6HA9i8kYTrmpgQeCyyHrn4mNOZSZwhoD0talTZiN-jINycHsc0IcduJOQg1stgk7bw8TRpOSt6hWKWyWyd0yl_WMCdGch_L2yxieyXz6nTE_gcYZItjrU2Av1LxLI0jNHfdHPMOsyLEGJkcsCd01bS6eCJ4VwomqIMM0OFnn8oWdT7QDrFg9r_K4-z9fYSOyrYr1Y1Ee6YzyJFgyRtoOCaiSr8hAWrPyUPk5abkHkI5MIz7vMNjvd2hh51gI7rKKGJ64iSXIOQmVuRnHrqrkZIe_6eZ4HVCMdKv7KB_eh043sV-j3zecNPR1kaqRnDkehZRsuYjtdv8FkgRbwV98HNFgMD9vYtWNh7tpPAOq3cHDnRgc4Z3cHT7yVqt-2vFdB7R4O3VV8OTktED4Dqhnw6KvXhG2zAD_sAk_5TciOZ1f4otSk7BjauQ9LHMC2uAQIA6sRv46iv7icDkdnnu4kMRgVsQiVdjDztLBL6_msvDWpnuxc-mCtjG76gcRIP5nq_r6gbKnlleJPnFtswg8nw_9bo1iSdLRiqq8R92ymCAmTvMM=w1510-h1422-s-no?authuser=0)
> - 만약 운영디비로 프로필을 바꾸고 접속이 거부된다면 클라우드에서 프로젝트가 8080포트로 실행됐을 수도 있다.
> - 이 경우에는 8080포트와 80포트를 포워딩하여 해결하는 방법이 있다.
> - `iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080`

**[특이사항]**

구현 과정에서 아쉬웠던 점 / 궁금했던 점을 정리합니다.

- 아직 nginx proxy manager가 동작하고 있지 않아서 http 연결로 동작한다. (그래서 OAuth와 같은 인증이 안된다.)
- 프로젝트를 하나의 도커컨테이너 실행시키는 것도 좋은 방법일 것 같다.
- gradlew로 실행시키는 방법과 jar를 java로 실행시키는 방법도 있던데 둘이 어떻게 차이가 있는걸까
- modifyDate와 현재시간을 비교하여 쿨타임을 재는 것이 간단하지만 테스트를 위해 리포지토리에 생짜쿼리를 날리는 메서드를 만들어야 하는 게 단점인 것 같다.
- 알림기능은 이벤트와 이벤트리스너를 이용하여 구현했다. 이벤트 리스너의 경우 엔티티별로 여러개를 만들 수가 있는걸까?
- 현재 InstaMemberEventListener가  InstaMember 엔티티의 로직을 처리하고 NotificationService를 통해 알림까지 발송한다.
- XXXEventListener가 엔티티마다가 존재해서 해당 엔티티에 관련된 로직만을 처리할 수 있는 걸까