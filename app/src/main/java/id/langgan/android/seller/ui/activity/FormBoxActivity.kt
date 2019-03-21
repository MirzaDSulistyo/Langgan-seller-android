package id.langgan.android.seller.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.langgan.android.seller.AppExecutors
import id.langgan.android.seller.R
import id.langgan.android.seller.databinding.ActivityFormBoxBinding
import id.langgan.android.seller.di.Injectable
import id.langgan.android.seller.model.Auth
import id.langgan.android.seller.utility.Prefs
import id.langgan.android.seller.viewmodel.BoxViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

class FormBoxActivity : AppCompatActivity(), HasSupportFragmentInjector, Injectable {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: BoxViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var binding: ActivityFormBoxBinding

    private var auth: Auth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_form_box)

        val prefs = Prefs()
        prefs.context = this
        auth = Gson().fromJson(prefs.user, Auth::class.java)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(BoxViewModel::class.java)
        viewModel.setAuth(auth?.token)
        viewModel.setStoreId(auth?.stores!![0].id)

        binding.lifecycleOwner = this

        binding.close.setOnClickListener { finish() }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

}
