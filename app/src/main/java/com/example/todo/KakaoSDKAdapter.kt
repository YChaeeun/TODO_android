package com.example.todo

import com.kakao.auth.*

// 카카오 개발 가이드
// https://developers.kakao.com/docs/android/user-management#%EB%A1%9C%EA%B7%B8%EC%9D%B8
class KakaoSDKAdapter : KakaoAdapter() {

    // Application이 가지고있는 정보를 얻기위한 interface.
    override fun getApplicationConfig(): IApplicationConfig {
        return IApplicationConfig {
            GlobalApplication.instance?.getGlobalApplicationContext()
        }
    }

    // 로그인을 위해 Session을 생성하기 위해 필요한 옵션을 얻기위한 abstract class.
    override fun getSessionConfig(): ISessionConfig {
        return object : ISessionConfig {
            override fun getAuthTypes(): Array<AuthType> { // 로그인 시 인증받을 타입 지정
                return arrayOf(AuthType.KAKAO_TALK)
            }

            override fun isUsingWebviewTimer(): Boolean { // CPU 소모 절약, 디폴트 false
                return false
            }

            override fun isSecureMode(): Boolean { //로그인시 access token과 refresh token을 저장할 때의 암호화 여부를 결정한다.
                return true
            }

            override fun getApprovalType(): ApprovalType? { // 카카오 제휴 앱일 경우 사용, 디폴트 ApprovalType.INDIVIDUAL
                return ApprovalType.INDIVIDUAL
            }

            override fun isSaveFormData(): Boolean { // WebView에서 email 입력폼에서 data를 save할지 여부, 디폴트 true
                return true
            }
        }
    }
}