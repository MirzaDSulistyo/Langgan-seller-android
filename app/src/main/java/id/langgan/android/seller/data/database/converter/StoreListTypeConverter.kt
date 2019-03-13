package id.langgan.android.seller.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.langgan.android.seller.model.Store
import java.util.*

object StoreListTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToStoreList(data: String?) : List<Store>? {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType =  object : TypeToken<List<Store>>() {}.type

        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    @JvmStatic
    fun storeListToString(data: List<Store>?): String? {
        return Gson().toJson(data)
    }
}