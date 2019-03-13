package id.langgan.android.seller.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import id.langgan.android.seller.data.api.ApiService
import id.langgan.android.seller.data.database.AppDb
import id.langgan.android.seller.data.database.dao.BoxDao
import id.langgan.android.seller.data.database.dao.ProductDao
import id.langgan.android.seller.data.database.dao.UserDao
import id.langgan.android.seller.utility.LiveDataCallAdapterFactory
import id.langgan.android.seller.utility.Vars
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    /********
     * URLS
     */
    private val LOCAL = Vars.getLocale()

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://tranquil-shore-53254.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDb {
        return Room
            .databaseBuilder(app, AppDb::class.java, "langgan-seller.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDb): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideProductDao(db: AppDb): ProductDao {
        return db.productDao()
    }

    @Singleton
    @Provides
    fun provideBoxDao(db: AppDb): BoxDao {
        return db.boxDao()
    }

}