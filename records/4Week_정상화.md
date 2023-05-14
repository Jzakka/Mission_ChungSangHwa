# 4Week_정상화.md

## Title: [4Week] 정상화

### 미션 요구사항 분석 & 체크리스트

---

### 필수 과제
> - [x] 네이버클라우드 플랫폼으로 배포, 도메인, https 적용
> - [X] 내가 받은 호감리스트에서 성별 필터

### 선택과제
> - [x] 젠킨스로 CI/CD
> - [X] 내가 받은 호감리스트에서 호감사유 필터링
> - [X] 내가 받은 호감리스트 정렬 기능
 
### 4주차 미션 요약  

---

**[접근 방법]**

체크리스트를 중심으로 각각의 기능을 구현하기 위해 어떤 생각을 했는지 정리합니다.
> ### 네이버 클라우드 배포
> - https://melike.lwu.me
> - 3주차 배포 때,  도메인연결&nginx로 https 적용

> ### 필터링&정렬
> - InstaMember 엔티티에 정렬&필터링 메서드 추가
> - 애플리케이션에서 정렬&필터링

> ### 젠킨스 CI/CD
> - 젠킨스 컨테이너에서 애플리케이션 실행
> - 처음엔 젠킨스 컨테이너 안에서 그램그램 컨테이너를 실행하려고 했는데 포트 충돌이 났다.
>   ![Untitled](https://github.com/Jzakka/Mission_ChungSangHwa/assets/105845911/638fa159-2f43-4f60-9794-647e886fd738)
> - 젠킨스의 8080포트를 gramgram 컨테이너의 8080포트로 포워딩하려 했으나 <span style="color:red">실패</span>(젠킨스 8080포트는 이미 젠킨스 설정 GUI에 사용됨)
> - 그래서 젠킨스 80포트를 gramgram컨테이너의 8080포트로 포워딩하려 했으나 <span style="color:red">실패</span>(80포트를 사용중인 곳이 없고, 이 80포트는 호스트의 8080포트와 포워딩돼있기만 함. 왜 포트 충돌이 나는지 모르겠음)
> - 심지어 8081포트도 포트충돌이 남. (사용중이지 않는데도)
> - 원인이랑 해결책을 모르겠어서 젠킨스컨테이너에서 직접 WAS를 돌리기로함
> - 포어그라운드로 WAS를 실행하면 젠킨스 빌드가 무한히 돌아가는 문제 발생 -> 백그라운드 시도
> - 백그라운드로 WAS를 실행하면 빌드는 성공하나 <span style="color:orange">프로세스 목록에 애플리케이션이 없음</span>
> - 젠킨스는 job이 끝날 때, 실행시켰던 프로세스를 모두 삭제시키는 것이 원인 [JenkinsProcessTreeKiller](https://wiki.jenkins.io/display/JENKINS/ProcessTreeKiller.html)
> - 프리스타일의 쉘스크립트에선 환경변수 BUILD_ID, 파이프라인에서는 JENKINS_NODE_COOKIE를 dontKillMe로 설정

**파이프라인 스크립트**
```groovy
pipeline {
    agent any
    
    tools {
        jdk 'openjdk-17-jdk'
    }
    
    stages {
        stage('Prepare') {
            steps {
                git branch: '4Week',
                    url: 'https://github.com/Jzakka/Mission_ChungSangHwa'
            }
            
            post {
                success { 
                    sh 'echo "Successfully Cloned Repository"'
                }
                failure {
                    sh 'echo "Fail Cloned Repository"'
                }
            }    
        }
        
        
        stage('Build Gradle Test') {
            
            steps {
                sh (script:'''
                    echo "Build Gradle Test Start"
                ''')

                dir('.') {
                    sh """
                    chmod +x gradlew
                    """
                }
                
                dir('.') {
                    sh """
                    ./gradlew clean build
                    """
                }
            }
            
            post {
                success { 
                    sh 'echo "Successfully Build Gradle Test"'
                }
                 failure {
                    sh 'echo "Fail Build Gradle Test"'
                }
            }    
        }
        
        
        stage('Deploy') {
            steps {
                sh 'echo "deploy"'
                sh """
                export MYSQL_PW='MYSQL_PW'
                export KAKAO_CL_ID='KAKAO_CL_ID'
                export GOOGLE_CL_ID='GOOGLE_CL_ID'
                export GOOGLE_CL_SC='GOOGLE_CL_SC'
                export NAVER_CL_ID='NAVER_CL_ID'
                export NAVER_CL_SC='NAVER_CL_SC'
                export INSTA_CL_ID='INSTA_CL_ID'
                export INSTA_CL_SC='INSTA_CL_SC'
                export FACEBOOK_CL_ID='FACEBOOK_CL_ID'
                export FACEBOOK_CL_SC='FACEBOOK_CL_SC'

                export JENKINS_NODE_COOKIE=dontKillMe && nohup java -jar -Dspring.profiles.active=prod build/libs/gramgram*SNAPSHOT.jar > nohup.out 2>& 1 &
                echo "jenkins node cookie is ${JENKINS_NODE_COOKIE}"
                echo "executed jar"
                """
            }
            
            post {
                success {
                    sh 'echo "Execute"'
                }

                failure {
                    sh 'echo "Fail"'
                }
            }
        }
    }
}
```
 

**[특이사항]**

구현 과정에서 아쉬웠던 점 / 궁금했던 점을 정리합니다.

- 정렬&필터링을 쿼리Dsl을 이용해서 db에서 이미 가공된 데이터를 얻어오는 방법도 좋을 것 같다
- nginx가 http 요청을 https로 변환해 주는 과정이 잘 이해되지 않는다.
- 젠킨스 내부에서 컨테이너를 실행할 때 포트충돌이 일어나는 이유를 모르겠다. 
- 강사님의 예제에서는 8080으로 포트포워딩을 하셨는데 이건 왜 포트충돌이 안나는지 모르겠다(8080은 이미 젠킨스에서 사용되고 있어서 포트충돌이 나야하는 거 아닌지)