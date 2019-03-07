package id.langgan.android.seller.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import id.langgan.android.seller.ui.activity.LoginActivity
import id.langgan.android.seller.ui.activity.MainActivity

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeLoginActivity(): LoginActivity

}