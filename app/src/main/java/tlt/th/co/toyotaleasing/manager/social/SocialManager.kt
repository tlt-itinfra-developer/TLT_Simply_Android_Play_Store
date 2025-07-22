package tlt.th.co.toyotaleasing.manager.social

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.linecorp.linesdk.LineApiResponseCode
import com.linecorp.linesdk.LineProfile
import com.linecorp.linesdk.auth.LineLoginApi
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.manager.JsonMapperManager

import tlt.th.co.toyotaleasing.model.FacebookLogin
import java.util.*


class SocialManager private constructor() {

    private val LINE_SIGNIN_REQUEST_CODE: Int = 1
    private val GOOGLE_SIGNIN_REQUEST_CODE: Int = 2
    private val FACEBOOK_SIGNIN_REQUEST_CODE: Int = 64206
    private val facebookRequestPermissions = Arrays.asList("public_profile", "email")

    private lateinit var facebookCallbackManager: CallbackManager
    private lateinit var socialCallback: SocialCallback

    companion object {
        private val socialManager = SocialManager()
        fun getInstance(socialCallback: SocialCallback): SocialManager {
            socialManager.socialCallback = socialCallback
            return socialManager
        }
    }

    fun googleLogin(fragment: Fragment) {
        //DatabaseManager.getInstance().updateSocialLoginState(isSocialLoginState = true)
        val account = GoogleSignIn.getLastSignedInAccount(fragment.context)
        account?.let {
            socialCallback.onSuccess(getGoogleSignInProfile(account))
            return
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(fragment.context!!, gso)
        val signInIntent = googleSignInClient.signInIntent

        fragment.startActivityForResult(signInIntent, GOOGLE_SIGNIN_REQUEST_CODE)
    }

    fun facebookLogin(fragment: Fragment) {
        //DatabaseManager.getInstance().updateSocialLoginState(isSocialLoginState = true)
        facebookCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(fragment, facebookRequestPermissions)
        LoginManager.getInstance().registerCallback(facebookCallbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        getFacebookProfile { data ->
                            socialCallback.onSuccess(data)
                        }
                    }

                    override fun onCancel() {
                        socialCallback.onFailure("Your cancel.")
                    }

                    override fun onError(exception: FacebookException) {
                        socialCallback.onFailure(exception.message!!)
                    }
                })
    }

    fun lineLogin(fragment: Fragment) {
        //DatabaseManager.getInstance().updateSocialLoginState(isSocialLoginState = true)
        val loginIntent = LineLoginApi.getLoginIntent(
                fragment.context!!,
                BuildConfig.LINE_CHANNEL_ID)
        fragment.startActivityForResult(loginIntent, LINE_SIGNIN_REQUEST_CODE)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            FACEBOOK_SIGNIN_REQUEST_CODE -> facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
            GOOGLE_SIGNIN_REQUEST_CODE -> {
                val completedTask = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = completedTask.getResult(ApiException::class.java)
                    socialCallback.onSuccess(getGoogleSignInProfile(account))
                } catch (e: ApiException) {
                    socialCallback.onFailure(e.message!!)
                }
            }
            LINE_SIGNIN_REQUEST_CODE -> {
                val result = LineLoginApi.getLoginResultFromIntent(data)

                when (result.responseCode) {
                    LineApiResponseCode.SUCCESS -> socialCallback.onSuccess(getLineProfile(result.lineProfile!!))
                    LineApiResponseCode.CANCEL -> socialCallback.onFailure(result.errorData.message!!)
                    else -> socialCallback.onFailure(result.errorData.message!!)
                }
            }
        }
    }

    private fun getGoogleSignInProfile(account: GoogleSignInAccount?): SocialProfile? {
        account?.let {
            return SocialProfile(
                    account.id ?: "",
                    account.displayName ?: "",
                    account.familyName ?: "",
                    account.givenName ?: "",
                    account.email ?: "",
                    "",
                    account.photoUrl.toString(),
                    account.idToken ?: "",
                    "",
                    SocialProfile.GOOGLE
            )
        }
        return null
    }

    private fun getFacebookProfile(callback: (data: SocialProfile) -> Unit) {
        AccessToken.getCurrentAccessToken()?.let { accessToken ->
            val request = GraphRequest.newMeRequest(accessToken) { `object`, _ ->

                if (`object` != null) {
                    val facebookResponse = JsonMapperManager.getInstance().gson
                            .fromJson(`object`.toString(), FacebookLogin::class.java)

                    val socialProfile = SocialProfile(
                            facebookResponse.id,
                            "${facebookResponse.first_name} ${facebookResponse.last_name}",
                            "",
                            "",
                            facebookResponse.email,
                            "",
                            facebookResponse.picture.facebookPicture.url,
                            accessToken.token,
                            "",
                            SocialProfile.FACEBOOK
                    )

                    callback.invoke(socialProfile)
                } else {
                    callback.invoke(SocialProfile())
                }
            }

            val parameters = Bundle()
            parameters.putString("fields", "picture.type(large),email,first_name,last_name")
            request.parameters = parameters
            request.executeAsync()
        }
    }

    private fun getLineProfile(lineProfile: LineProfile): SocialProfile? {
        return SocialProfile(
                lineProfile.userId,
                lineProfile.displayName,
                "",
                "",
                "",
                "",
                lineProfile.pictureUrl.toString(),
                "",
                lineProfile.statusMessage ?: "",
                SocialProfile.LINE
        )
    }
}