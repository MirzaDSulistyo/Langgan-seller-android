package id.langgan.android.seller.data.api

import id.langgan.android.seller.model.Auth
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    /* ====== AUTHENTICATION ===== */

    @FormUrlEncoded
    @POST("users/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Auth>
}