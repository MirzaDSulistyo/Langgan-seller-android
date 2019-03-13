package id.langgan.android.seller.repository

import androidx.lifecycle.LiveData
import id.langgan.android.seller.AppExecutors
import id.langgan.android.seller.data.api.ApiService
import id.langgan.android.seller.data.database.AppDb
import id.langgan.android.seller.data.database.dao.BoxDao
import id.langgan.android.seller.data.vo.Resource
import id.langgan.android.seller.model.Box
import id.langgan.android.seller.model.BoxList
import id.langgan.android.seller.utility.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoxRepository
@Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: AppDb,
    private val boxDao: BoxDao,
    private val apiService: ApiService
)
{
    private val rateLimit = RateLimiter<String>(2, TimeUnit.MINUTES)

    fun getBoxes(token: String, id: String): LiveData<Resource<List<Box>>> {
        return object : NetworkBoundResource<List<Box>, BoxList>(appExecutors) {
            override fun saveCallResult(item: BoxList) {
                db.beginTransaction()
                try {
                    boxDao.insertBoxes(item.boxes!!)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Box>?): Boolean {
                return true
            }

            override fun createCall() = apiService.getBox(token, id)

            override fun loadFromDb() = boxDao.load()

            override fun onFetchFailed() {
                rateLimit.reset(token)
            }
        }.asLiveData()
    }
}