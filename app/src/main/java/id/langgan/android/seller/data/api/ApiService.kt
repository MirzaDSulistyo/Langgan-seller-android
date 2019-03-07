package id.langgan.android.seller.data.api

import id.langgan.android.seller.model.Auth
import id.langgan.android.seller.model.Owner
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

    @FormUrlEncoded
    @POST("users/registerowner")
    fun registerOwner(
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") name: String
    ): Call<Owner>
}