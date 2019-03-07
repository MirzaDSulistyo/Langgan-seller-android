package id.langgan.android.seller.ui.activity

import android.app.Activity
import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import id.langgan.android.seller.R
import id.langgan.android.seller.di.AppInjector
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import javax.inject.Inject

class MainApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Lato-Regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build())
                )
                .build())

        AppInjector.init(this)
    }

    override fun activityInjector() =  dispatchingAndroidInjector

}