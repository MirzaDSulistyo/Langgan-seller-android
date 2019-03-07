package id.langgan.android.seller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.langgan.android.seller.data.vo.Resource
import id.langgan.android.seller.model.Auth
import id.langgan.android.seller.repository.UserRepository
import javax.inject.Inject

class UserViewModel
@Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun login(email: String, pass: String): LiveData<Resource<Auth>> {
        return userRepository.login(email, pass)
    }

}