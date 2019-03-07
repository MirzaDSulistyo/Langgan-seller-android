package id.langgan.android.seller.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import id.langgan.android.seller.model.Store

object StoreTypeConverter {

    var gson = Gson()

    @TypeConverter
    @JvmStatic
    fun stringToStore(data: String?) : Store {

        return gson.fromJson(data, Store::class.java)
    }

    @TypeConverter
    @JvmStatic
    fun storeToString(data: Store?): String? {
        return gson.toJson(data)
    }

}