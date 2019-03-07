package id.langgan.android.seller.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.langgan.android.seller.AppExecutors
import id.langgan.android.seller.data.api.*
import id.langgan.android.seller.data.database.AppDb
import id.langgan.android.seller.data.database.dao.UserDao
import id.langgan.android.seller.data.vo.Resource
import id.langgan.android.seller.model.Auth
import id.langgan.android.seller.utility.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository
@Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: AppDb,
    private val userDao: UserDao,
    private val apiService: ApiService
) {
    private val rateLimit = RateLimiter<String>(2, TimeUnit.MINUTES)

    fun login(email: String, pass: String): LiveData<Resource<Auth>> {

        val data = MutableLiveData<Resource<Auth>>()

        appExecutors.networkIO().execute {
            val response = apiService.login(email, pass).execute()
            val apiResponse = ApiResponse.create(response)

            when (apiResponse) {
                is ApiSuccessResponse -> {
                    data.postValue(Resource.success(apiResponse.body))
                }
                is ApiEmptyResponse -> {
                    data.postValue(Resource.success(data = null))
                }
                is ApiErrorResponse -> {
                    data.postValue(Resource.error(apiResponse.errorMessage, null))
                }
            }
        }

        return data
    }
}