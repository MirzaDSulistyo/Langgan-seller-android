package id.langgan.android.seller.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import id.langgan.android.seller.viewmodel.BoxViewModel
import id.langgan.android.seller.viewmodel.ProductViewModel
import id.langgan.android.seller.viewmodel.UserViewModel
import id.langgan.android.seller.viewmodel.ViewModelFactory

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindDataUserViewModel(user: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel::class)
    abstract fun bindDataProductViewModel(product: ProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BoxViewModel::class)
    abstract fun bindDataBoxViewModel(box: BoxViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}