<br>

> <strong>[[최종 발표 보고서]](https://docs.google.com/presentation/d/1Z4JqRWb_bJ9chjXlaJHKi1FcEBBmONWTRP2w7cXDWvs/edit?usp=sharing)</strong><br>

<br>

![1](https://github.com/user-attachments/assets/8a3bb4f1-f63d-4295-8ff4-8c6a7baf2014)

![16](https://github.com/user-attachments/assets/b9f2dfc3-c17b-4802-9808-8c2d0497ff95)

![17](https://github.com/user-attachments/assets/e5f86589-1ce2-47f5-ac9e-1ba254381473)

![14](https://github.com/user-attachments/assets/760efbae-6acd-4758-b90f-7ec63771b989)

![9](https://github.com/user-attachments/assets/6362dd00-c3cc-49fa-8d0a-b6ebba9d7e51)

![10](https://github.com/user-attachments/assets/885bcfec-1b78-46ca-a7dc-1c03e0ac6faf)

![4](https://github.com/user-attachments/assets/30bbf54d-3cce-43ed-b69e-c2a6130a9348)

![5](https://github.com/user-attachments/assets/fcc9200a-5c89-4fc7-9557-61352d3236e5)

![6](https://github.com/user-attachments/assets/81391edd-a78e-4946-ac5b-bedc0608047a)

![7](https://github.com/user-attachments/assets/fc269142-6a98-4549-babc-e033b04857a0)

![11](https://github.com/user-attachments/assets/482c228a-c341-4f6d-9737-1fdb54a38e76)

![12](https://github.com/user-attachments/assets/68c42d17-eba5-487c-b57d-8782ddf3d38c)

![13](https://github.com/user-attachments/assets/c6f1aadf-b580-419e-a7e4-3f5be0b6a3d3)


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
