package id.langgan.android.seller.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.langgan.android.seller.data.database.converter.StoreTypeConverter
import id.langgan.android.seller.data.database.converter.UserTypeConverter

@Entity
@TypeConverters(UserTypeConverter::class, StoreTypeConverter::class)
class Owner {
    @SerializedName("auth")
    @Expose
    var auth: Boolean? = null

    @PrimaryKey
    @SerializedName("expiresIn")
    @Expose
    var expiresIn: Int = 0

    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("user")
    @Expose
    var user: User? = null

    @SerializedName("store")
    @Expose
    var store: Store? = null
}