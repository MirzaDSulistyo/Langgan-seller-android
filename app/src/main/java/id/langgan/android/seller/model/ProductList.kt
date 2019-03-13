package id.langgan.android.seller.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductList {

    @SerializedName("data")
    @Expose
    var products: List<Product>? = null

    @SerializedName("status")
    @Expose
    val status: Int = 0

}