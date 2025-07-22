package tlt.th.co.toyotaleasing.manager.api.shoppening

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.manager.ContextManager
import java.util.concurrent.TimeUnit

class ShoppeningApiManager private constructor() {

    private val service: ShoppeningApiService

    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.SHOPPENING_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        service = retrofit.create(ShoppeningApiService::class.java)
    }

    fun getOil(callback: (isError: Boolean, result: String) -> Unit) {
        service.getOil()
                .enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        callback(true, t.message ?: "")
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (!response.isSuccessful) {
                            callback(true, "")
                            return
                        }

                        callback(false, response.body() ?: "")
                    }
                })
    }

    companion object {
        private val httpManagerInstance = ShoppeningApiManager()
        fun getInstance() = httpManagerInstance
    }
}