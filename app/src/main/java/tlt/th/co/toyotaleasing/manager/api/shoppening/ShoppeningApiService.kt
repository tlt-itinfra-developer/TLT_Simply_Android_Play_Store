package tlt.th.co.toyotaleasing.manager.api.shoppening

import retrofit2.Call
import retrofit2.http.GET

interface ShoppeningApiService {

    @GET("widget/home/")
    fun getOil(): Call<String>
}