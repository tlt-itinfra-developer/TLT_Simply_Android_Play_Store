package tlt.th.co.toyotaleasing.manager.api.tlt

import com.google.firebase.iid.FirebaseInstanceId
import okhttp3.Interceptor
import okhttp3.Response
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.manager.db.UserManager

class TLTApiRequestInterceptor : Interceptor {

    private val userManager = UserManager.getInstance()

    override fun intercept(chain: Interceptor.Chain?): Response {
        val original = chain!!.request()

        val requestBuilder = original.newBuilder()
        val url = original.url().toString()

        when {
            isRegisterNonCustomer(url) -> {
                val token = FirebaseInstanceId.getInstance().token
                requestBuilder.addHeader("Authorization", "Basic $token")
            }
            isApiRegisterFlow(url) -> {
                val accessToken = userManager.getProfile().token
                requestBuilder.addHeader("Authorization", "Basic $accessToken")
            }
            isRegisterNonStaff(url) -> {
                val token = FirebaseInstanceId.getInstance().token
                requestBuilder.addHeader("Authorization", "Basic $token")
            }
            else -> {
                val accessToken = userManager.getProfile().token
                requestBuilder.addHeader("Authorization", "Bearer $accessToken")
            }
        }

        return chain.proceed(requestBuilder.build())
    }

    private fun isRegisterNonCustomer(url: String): Boolean {
        return url == "${BuildConfig.BASE_URL}Application/RegisNonCustomer"
    }

    private fun isRegisterNonStaff(url: String): Boolean {
        return url == "${BuildConfig.BASE_URL}Application/RegisNonStaff"
    }

    private fun isApiRegisterFlow(url: String): Boolean {
        return when (url) {
            "${BuildConfig.BASE_URL}Application/CustRegisLoad" -> true
            "${BuildConfig.BASE_URL}Application/SocialRegister" -> true
            "${BuildConfig.BASE_URL}Application/SendEmailRegis" -> true
            "${BuildConfig.BASE_URL}Application/GetPhonenumber" -> true
            "${BuildConfig.BASE_URL}Application/SendOTPRegis" -> true
            "${BuildConfig.BASE_URL}Application/VerifyOTPRegis" -> true
            "${BuildConfig.BASE_URL}Application/BeforeRegister" -> true
            "${BuildConfig.BASE_URL}Application/SetPINRegis" -> true
            "${BuildConfig.BASE_URL}Application/GetBanner" -> true
            "${BuildConfig.BASE_URL}Application/CheckMasterLoad" -> true
            "${BuildConfig.BASE_URL}Application/UpdateMasterLoad" -> true
            "${BuildConfig.BASE_URL}Application/AcceptTermCondition" -> true
            "${BuildConfig.BASE_URL}Application/CheckVerifyEmail" -> true
            "${BuildConfig.BASE_URL}Application/UpdateToken" -> true
            else -> false
        }
    }

    private fun isCustomer() = userManager.isCustomer()
}