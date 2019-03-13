package id.langgan.android.seller.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.langgan.android.seller.data.database.dao.BoxDao
import id.langgan.android.seller.data.database.dao.ProductDao
import id.langgan.android.seller.data.database.dao.UserDao
import id.langgan.android.seller.model.Auth
import id.langgan.android.seller.model.Box
import id.langgan.android.seller.model.Product
import id.langgan.android.seller.model.User

/**
 * Main database description.
 */
@Database(
    entities = [
        User::class,
        Product::class,
        Box::class,
        Auth::class],
    version = 2,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun productDao(): ProductDao

    abstract fun boxDao(): BoxDao

}