package com.example.todo

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.kakao.auth.ISessionCallback
import com.kakao.auth.KakaoSDK
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.kakao.auth.Session

import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity() {



    private var callback: SessionCallback = SessionCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
            .replace(R.id.layout_login_container, LoginFragment() )
            .commit()

        //getHash()


        Session.getCurrentSession().addCallback(callback)
    }


    private fun getHash() {
        Log.e("ggg", "hello")
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = info.signingInfo.apkContentsSigners
            val md = MessageDigest.getInstance("SHA")
            for (signature in signatures) {
                md.update(signature.toByteArray())
                val key = String(Base64.encode(md.digest(), 0))
                Log.d("Hash key:", "!!!!!!!$key!!!!!!")
            }
        } catch (e: Exception) {
            Log.e("name not found", e.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.d("current session","")
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private class SessionCallback : ISessionCallback {

        // memory와 cache에 session 정보가 전혀 없는 상태.
        // 일반적으로 로그인 버튼이 보이고 사용자가 클릭시 동의를 받아 access token 요청을 시도한다.
        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.e("onSessionOpenFailed", "${exception?.message}")
        }


        // access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태.
        // 일반적으로 로그인 후의 다음 activity로 이동한다.
        override fun onSessionOpened() {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {

                override fun onFailure(errorResult: ErrorResult?) {
                    Log.d("Call back failed", "${errorResult?.errorMessage}")
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Log.e("onSessionClosed", "${errorResult?.errorMessage}")

                }

                override fun onSuccess(result: MeV2Response?) {
                    checkNotNull(result) { "session response null" }
                    // register or login
                }

            })
        }

    }


}
