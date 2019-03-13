package id.langgan.android.seller.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.langgan.android.seller.ui.fragment.BoxFragment
import id.langgan.android.seller.ui.fragment.ProductFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeProductFragment(): ProductFragment

    @ContributesAndroidInjector
    abstract fun contributeBoxFragment(): BoxFragment

}