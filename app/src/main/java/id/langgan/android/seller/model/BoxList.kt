package id.langgan.android.seller.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BoxList {

    @SerializedName("data")
    @Expose
    var boxes: List<Box>? = null

    @SerializedName("status")
    @Expose
    val status: Int = 0

}