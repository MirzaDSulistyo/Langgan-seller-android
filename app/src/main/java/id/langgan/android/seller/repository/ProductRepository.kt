package id.langgan.android.seller.repository

import androidx.lifecycle.LiveData
import id.langgan.android.seller.AppExecutors
import id.langgan.android.seller.data.api.ApiService
import id.langgan.android.seller.data.database.AppDb
import id.langgan.android.seller.data.database.dao.ProductDao
import id.langgan.android.seller.data.vo.Resource
import id.langgan.android.seller.model.Product
import id.langgan.android.seller.model.ProductList
import id.langgan.android.seller.utility.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository
@Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: AppDb,
    private val productDao: ProductDao,
    private val apiService: ApiService
)
{
    private val rateLimit = RateLimiter<String>(2, TimeUnit.MINUTES)

    fun getProducts(token: String, id: String): LiveData<Resource<List<Product>>> {
        return object : NetworkBoundResource<List<Product>, ProductList>(appExecutors) {
            override fun saveCallResult(item: ProductList) {
                db.beginTransaction()
                try {
                    productDao.insertProducts(item.products!!)
                    db.setTransactionSuccessful()
                } finally {
                    db.endTransaction()
                }
            }

            override fun shouldFetch(data: List<Product>?): Boolean {
                return true
            }

            override fun createCall() = apiService.getProduct(token, id)

            override fun loadFromDb() = productDao.load()

            override fun onFetchFailed() {
                rateLimit.reset(token)
            }
        }.asLiveData()
    }
}