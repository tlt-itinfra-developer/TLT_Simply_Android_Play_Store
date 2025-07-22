package tlt.th.co.toyotaleasing.manager.api.tlt

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class TLTApiResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain!!.request()
        val response = chain.proceed(request)

        when {
            response.isSuccessful -> Log.d("HttpInterceptor", "TLTApiResponseInterceptor Success")
            else -> Log.d("HttpInterceptor", "TLTApiResponseInterceptor Failure")
        }

        return response
    }

}