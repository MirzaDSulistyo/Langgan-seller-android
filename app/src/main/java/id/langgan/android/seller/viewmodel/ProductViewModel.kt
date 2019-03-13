package id.langgan.android.seller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import id.langgan.android.seller.data.vo.Resource
import id.langgan.android.seller.model.Product
import id.langgan.android.seller.repository.ProductRepository
import id.langgan.android.seller.utility.AbsentLiveData
import javax.inject.Inject

class ProductViewModel
@Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel()
{
    private val _token = MutableLiveData<String>()
    private val _id = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    val id: LiveData<String>
        get() = _id

    fun setAuth(token: String?) {
        if (_token.value != token) {
            _token.value = token
        }
    }

    fun setStoreId(id: String?) {
        if (_id.value != id) {
            _id.value = id
        }
    }

    fun refresh() {
        _token.value?.let {
            _token.value = it
        }
    }

    val products: LiveData<Resource<List<Product>>> = Transformations
        .switchMap(_token) {token ->
            if (token == null) {
                AbsentLiveData.create()
            } else {
                productRepository.getProducts(token, id.value!!)
            }
        }
}