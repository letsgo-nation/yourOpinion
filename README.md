![header](https://capsule-render.vercel.app/api?type=Waving&color=auto&height=200&section=header&text=너의%20의견은。&fontSize=90&fontColor=ffffff)
# 프로젝트 명 : Your Opinion

<div align="center">
  <h3> Flatforms & Languagges </h3>
  <img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white" />
	<img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white" />
	<img src="https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=CSS3&logoColor=white" />
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=white"/>
  <img src="https://img.shields.io/badge/jQuery-0769AD?style=flat&logo=jQuery&logoColor=white"/> <br/>
  <img src="https://img.shields.io/badge/thymeleaf-005F0F?style=flat&logo=thymeleaf&logoColor=white"/>
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=flat&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/springsecurity-6DB33F?style=flat&logo=springsecurity&logoColor=white"/>
  
  <br/>
  <h3> Tools </h3>
  <img src="https://img.shields.io/badge/intellijidea-000000?style=flat&logo=intellijidea&logoColor=white" />
	<img src="https://img.shields.io/badge/github-181717?style=flat&logo=github&logoColor=white" />
	<img src="https://img.shields.io/badge/apachetomcat-F8DC75?style=flat&logo=apachetomcat&logoColor=white" />
  <br/>
  <h3> Communication </h3>
  <img src="https://img.shields.io/badge/slack-4A154B?style=flat&logo=slack&logoColor=white" />
	<img src="https://img.shields.io/badge/notion-000000?style=flat&logo=notion&logoColor=white" />
</div>
<br/><br/>

#### - 팀명 : 2조 이름좀 바꿔조 <br/> 
#### - 팀원 : 임채영님(팀장) , 모성민님 , 소원님 , 이민우님
#### - 팀 규정 :
1. 오전 회의/오후 회의 시, 코드 진행 상황 보고 및 금일 할 일 보고
2. 지각 시에 벌칙 적용 ( <b> 메타버스 내에서 5바퀴 돌기 </b> )
3. 문제가 생길 경우 공유하여 소통을 통해 해결하기 -> 화면 공유를 통해 해결
4. github에 반영하기 이전에 화면을 보여주어 confirm 받고 반영하기.
5. develop 브랜치에 반영할 때에는 모든 팀원의 동의를 구하기.
6. Issue와 Commit 내역 연결하기

#### - 진행 과정 : [팀 노션](https://rowan-pufferfish-a5a.notion.site/372f2b524f1e42c99267a3aaa1efd8cc?pvs=4)
#### - 시연 영상 :  
 [![Video Label](http://img.youtube.com/vi/jh5lQzd4veA/0.jpg)](https://youtu.be/jh5lQzd4veA )
 
<br/>

## 기능 요구사항
### 1. 필수 기능
1) 로그인 및 로그아웃
<br/>2) 회원가입
<br/>3) 게시글 CRUD
<br/>4) 댓글 CRUD
<br/>5) 프로필 조회 및 수정
<br/><br/>

### 2. 추가 기능
1) 프론트엔드 구현
<br/>2) 게시글에 따른 투표 기능
<br/>3) 대댓글 CRUD
<br/>4) 회원 관리자 페이지 
<br/>5) 게시글 관리자 페이지 
<br/>6) 댓글 관리자 페이지
<br/><br/>

## API 설계
[API](https://rowan-pufferfish-a5a.notion.site/372f2b524f1e42c99267a3aaa1efd8cc?pvs=4)
<br/>

## ERD 구성
![image](https://github.com/Chaeyounglim/yourOpinion/assets/55676554/62bfdb2a-1f0f-4496-b841-e892dcce6a47)
<br/><br/><br/>

## 핵심 코드 리뷰
<br/>

### 1. 최근 사용한 3개의 비밀번호로 수정 불가하도록 합니다.
<br/>

```Java

    // 컬렉션과 같은 형태의 데이터를 컬럼에 저장할 수 없기에 별도의 테이블을 만들어 컬렉션을 관리하게하는 어노테이션
    @ElementCollection


    // 테이블명은 previous_passwords, 조인컬럼은 users_id
    @CollectionTable(name = "previous_passwords", joinColumns = @JoinColumn(name = "users_id"))
    

    @Column(name = "password") // column명은 password로 정한다.
    private List<String> previousPasswords = new ArrayList<>(); // 컬렉션의 종류중 하나인 ArrayList로 지정

----------------------------------------------------------------------------------------------------------------------------

      public pwChangeResponseDto changePassword(String username, pwChangeRequestDto updateProfile) {

        // user 변수에 레포지토리의 findbyusername JPA로 값을 가져온후 username인자로 받아온걸 변수로 저장한다.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다.")); // 만약 존재하지 않을경우 예외 발생

        // 만약 가져오면 Dto클래스에 인자값으로 가져온것을 get하여 ccheckpassword 변수에 저장
        String checkPassword = updateProfile.getCheckPassword();

        // 암호화되있는 비밀번호를 matches를 통해 암호화를 푼후 적은 checkpassword랑 user에 저장되어 있는 패스워드를 비교한다.
        if (!passwordEncoder.matches(checkPassword, user.getPassword())) { // 일치하지 않을 경우
            log.info(username + "님의 현재 비밀번호가 일치하지 않습니다."); // 테스트를 위한 로그
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다."); // 예외 발생
        }

        // 통과 후 newPassword 변수에 저장 
        String newPassword = updateProfile.getNewPassword();

         // 통과한 비밀번호와 previous_passwords테이블에 저장되있는 최근 3개의 비밀번호를 비교 isPreviousPassword객체는 따로 아래에 분리되어있음.
        if (isPreviousPassword(user, newPassword)) { 
            log.info(username + "님의 최근 변경한 3개의 비밀번호와 중복됩니다. 다른 비밀번호로 작성해주세요."); // 테스트를 위한 로그
            throw new IllegalArgumentException("최근 변경한 3개의 비밀번호와 중복됩니다. 다른 비밀번호로 작성해주세요."); // 예외 발생
        }

        // 모두 통과되면 hashedPassword변수에 새로운 패스워드를 암호화해서 저장
        String hashedPassword = passwordEncoder.encode(newPassword); 

         // 비밀번호 변경 시 이전 비밀번호를 기록하고 최대 3개까지 유지
        user.getPreviousPasswords().add(hashedPassword); // 어레이리스트에 새로운 비밀번호를 저장

        if (user.getPreviousPasswords().size() > 3) { // 만약 3개보다 많아질 경우
            user.getPreviousPasswords().remove(0); // 첫번째 비밀번호부터 제거
        }

        user.setPassword(hashedPassword); // 암호화된 패스워드를 엔티티에 저장
        user = userRepository.save(user); // 데이터에 담아줌.
        log.info(username + "님의 비밀번호 변경이 완료되었습니다.");
        return new pwChangeResponseDto(user); // DTO 타입으로 반환
    }





    private boolean isPreviousPassword(User user, String newPassword) {
        // 향상된 반복문으로 유저의 저장되있는 최근의 3개 비밀번호를 뺀후 임시 변수에 저장
        for (String prevPassword : user.getPreviousPasswords()) {
            //  비밀번호 암호화를 해제한 후 최근의 3개의 비밀번호와 변경하려는 비밀번호를 매치해봄
            if (passwordEncoder.matches(newPassword, prevPassword)) { 
                return true; // 일치하면 참을 반환한다.
            }
        }
        return false; // 일치하지않을경우 거짓을 반환한후 위에 if문에서 벗어나게됨.
    }
```
<br/><br/>

### 2. 투표 기능 구현 

### 1) DB 설계
- 게시글과 투표는 다대다 관계 이므로 투표에 대한 정보 테이블 별도로 생성
- 게시글 테이블에 불필요한 join 쿼리를 줄이기 위해 투표 합계 칼럼을 게시글 테이블에 새로 생성
  <br/>
  
![image](https://github.com/Chaeyounglim/yourOpinion/assets/55676554/32cc2c13-6908-470e-b6cf-941ccd61f6b8)
<br/><br/>

### 2) Service 코드
- 중복 투표를 방지하기 위해 서비스 클래스 내에서 DB의 정보를 불러와 양쪽 모두에 투표 정보가 없을 경우에만 투표가 가능
- 만약, 투표를 한 내역이 있다면 해당 옵션에 따른 '투표취소' 버튼 활성화
- 프론트뿐만이 아닌, 서버 코드에서도 옵션1을 투표를 시도할 경우, 옵션2에 투표한 내역이 있는지 판단하는 조건 추가
  <br/>
  
![image](https://github.com/Chaeyounglim/yourOpinion/assets/55676554/6d07446f-d294-48d8-beb8-90c7b5ce7987)
<br/><br/>

   
