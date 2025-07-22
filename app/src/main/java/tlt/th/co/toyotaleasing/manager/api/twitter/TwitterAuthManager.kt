package tlt.th.co.toyotaleasing.manager.api.twitter

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiRequestInterceptor
import java.util.concurrent.TimeUnit


class TwitterAuthManager private constructor() {

    private val BASE_URL = "https://api.twitter.com/"
    private val CONSUMER_KEY = ""
    private val CONSUMER_SECRET = ""
    private val USER_ID = ""

    private val service: TwitterService

    init {
        val base64 = getBase64EncodedBearerToken()
        val FORCE_REWRITE_CACHE_CONTROL_INTERCEPTOR = Interceptor { chain ->
            val originalRequest = chain.request()
            val request = originalRequest.newBuilder()
                    .header("Authorization", "Basic $base64")
            val response = chain.proceed(request.build())
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=2419220")
                    .build()
        }

        val client = OkHttpClient.Builder()
                .addInterceptor(TLTApiRequestInterceptor())
                .addInterceptor(FORCE_REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        service = retrofit.create(TwitterService::class.java)
    }

    fun getTwitterToken(grantType: String, callback: (isError: Boolean, result: String) -> Unit) {
        service.getTokenTwitter(grantType)
                .enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        callback(true, t.toString())
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        callback(false, response?.body().toString())
                    }

                })
    }

    fun getUserTimeLine(bearerKey: String, callback: (isError: Boolean, result: String) -> Unit) {
        val CACHE_CONTROL_INTERCEPTOR = Interceptor { chain ->
            val originalRequest = chain.request()
            val request = originalRequest.newBuilder()
                    .header("Content-OilType", "application/x-www-form-urlencoded")
                    .header("Authorization", "Bearer $bearerKey")
            val response = chain.proceed(request.build())
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=2419220")
                    .build()
        }

        val newClient = OkHttpClient.Builder()
                .addInterceptor(TLTApiRequestInterceptor())
                .addInterceptor(CACHE_CONTROL_INTERCEPTOR)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

        val newRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(newClient)
                .build()

        val service2 = newRetrofit.create(TwitterService::class.java)

        service2.getUserTimeline("100", USER_ID)
                .enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        callback(true, t.toString())
                    }

                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        callback(false, response?.body().toString())
                    }

                })
    }

    private fun getBase64EncodedBearerToken(): String {
        val bearerToken = "$CONSUMER_KEY:$CONSUMER_SECRET"
        val bytes = bearerToken.toByteArray()

        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    companion object {
        fun getInstance() = TwitterAuthManager()
    }
}