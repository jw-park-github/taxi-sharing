### 변경된 파일 목록
* register package 전체
* RegisterViewModel.kt
* MyFirestore.kt
* Repository.kt
* MainFragment.kt
* MyApplication.kt (new)
* NaverLogin 삭제 -> NaverIdLoginSdkExt.kt

## MyFirestore.kt
chkExist -> chkUnique : 검색결과가 없으면 true 반환    
repository 역시 마찬가지로 동작하도록 수정

## MainFragment.kt
유저의 앱 진입 시나리오별 버튼   

||클라이언트|서버|
|:--:|:----------------:|:--------:|
|  Case  | SharedPreference | Firestore|
| 로그인한 기존 사용자      | O | O |
| 로그인하지 않은 기존 사용자|X  |O  |
|신규 사용자|X|X|   

MainFragment.kt에서 시나리오별 상황을 재현하고 RegisterActivity.kt 실행

## RegisterActivity.kt, NaverLoginFragment.kt, NicknameFragment.kt
RegisterActivity.kt 에서 위 상황별로 로그인 시도
Fragment에서는 로그인 실행 후 MyApplication 전역 클래스에 유저 아이디 정보 전달, RegisterActivity 재시작

## MyApplication.kt
어플리케이션 전역에서 유지할 정보 저장 (SharedPref, UserId)   
**적용하려면 AndroidManifest 에서 application tag에 이름 지정해주어야 함**
```xml

```
 

## NaverIdLoginSdkExt.kt
기존 NaverLogin 클래스 삭제, extension으로 변경
