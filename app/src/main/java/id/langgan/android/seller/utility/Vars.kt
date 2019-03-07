package id.langgan.android.seller.utility

import java.util.*

object Vars {

    fun getLocale(): String {
        var locale = Locale.getDefault().language
        if (locale == "in")
            locale = "id"
        else
            locale = "en"

        return locale
    }

}