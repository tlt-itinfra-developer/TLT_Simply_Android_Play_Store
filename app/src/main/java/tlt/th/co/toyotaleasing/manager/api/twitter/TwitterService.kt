package tlt.th.co.toyotaleasing.manager.api.twitter

import retrofit2.Call
import retrofit2.http.*

interface TwitterService {

    @FormUrlEncoded
    @POST("oauth2/token")
    fun getTokenTwitter(@Field("grant_type") grantType: String): Call<String>

    @GET("1.1/statuses/user_timeline.json")
    fun getUserTimeline(@Query("count") count: String,
                        @Query("user_id") userId: String): Call<String>

}