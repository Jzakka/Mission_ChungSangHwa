## 체크리스트
*** 
 - [x] 호감목록에서  호감상대 삭제 구현
   - 컨트롤러에 삭제 url 맵핑, 서비스 구현
   - Rq 클래스에 redirectWithErrorMsg() 추가<br/><span style="color:gold">기존의 rq객체만으론 실패요청에 대해 노란 경고창을 나오게 할 수 없음</span>
 - [x] 구글 클라우드 플랫폼으로 OAuth 인증
   - appication.yml 에 google oauth 의 클라이언트 id와 클라이언트 secret, 리다이렉트 uri를 추가 
 - [x] .yml의 민감정보를 환경변수로 은폐
   - yml에 <span style="color:cyan">${환경변수명}</span> 으로 값을 넣어주면 시스템 환경변수에서 값을 읽어옴
   - Mac 사용자의 경우 .zshrc가 아닌 .bash_profile에서 설정해줘야함
   - 설정후 인텔리제이를 껐다 키면 환경변수가 적용됨

### .bash_profile에 환경변수 설정 예
```shell
export MYSQL_PW=비밀번호
export KAKAO_CL_ID=카카오_클라이언트_ID
export GOOGLE_CL_ID=구글_클라이언트_ID
export GOOGLE_CL_SC=구글_클라이언트_시크릿
#...
```
## 특이사항
***
- 주어진 Rq클래스를 직접적으로 수정하는 것 밖에 방법이 없었는가에 대해 다시 고민해 봐야할 듯 하다.
