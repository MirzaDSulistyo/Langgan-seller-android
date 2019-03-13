package id.langgan.android.seller.data.api

import androidx.lifecycle.LiveData
import id.langgan.android.seller.model.Auth
import id.langgan.android.seller.model.BoxList
import id.langgan.android.seller.model.Owner
import id.langgan.android.seller.model.ProductList
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
    @POST("users/loginowner")
    fun loginOwner(
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

    /* ====== PRODUCT ===== */

    @GET("product/list/{id}")
    fun getProduct(
        @Header("token") token: String,
        @Path("id") id: String
    ): LiveData<ApiResponse<ProductList>>

    /* ====== BOX ===== */

    @GET("box/list/{id}")
    fun getBox(
        @Header("token") token: String,
        @Path("id") id: String
    ): LiveData<ApiResponse<BoxList>>
}