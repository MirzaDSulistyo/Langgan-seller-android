package id.langgan.android.seller.utility

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import timber.log.Timber

object Helper {

    fun hideSoftKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getFirstTwoChars(text: String): String {
        val arrText = text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val strText = StringBuilder()
        for (i in arrText.indices) {
            if (i > 1) break
            if (arrText[i] != "") {
                strText.append(arrText[i][0])
            }
        }
        return strText.toString()
    }

    fun convert(value: String): Int {
        if (value.isNotEmpty()) {
            val v = value.replace(",", "")
            val len = v.length
            if (len > 2) {
                val start = len - 3
                val j = v.removeRange(start, len)
                var data = 0
                try {
                    data = j.toInt()
                } catch (e: NumberFormatException) {
                    Timber.d(e, "error")
                }
                return data
            }

            return 0
        }

        return 0
    }

}