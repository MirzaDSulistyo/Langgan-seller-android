package id.langgan.android.seller.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.langgan.android.seller.R
import id.langgan.android.seller.data.vo.Resource
import id.langgan.android.seller.data.vo.Status
import id.langgan.android.seller.model.Auth
import id.langgan.android.seller.utility.Helper
import id.langgan.android.seller.utility.InternetConnection
import id.langgan.android.seller.utility.Prefs
import id.langgan.android.seller.viewmodel.UserViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: UserViewModel

    private var hud: KProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(UserViewModel::class.java)

        login.setOnClickListener { onLogin() }

        signUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun onLogin() {
        Helper.hideSoftKeyboard(this@LoginActivity)

        if (InternetConnection.checkConnection(applicationContext)) {

            hud!!.show()

            val email = email.text.toString()
            val pass = password.text.toString()

            viewModel.login(email, pass)
                .observe(this, Observer<Resource<Auth>> {
                    if (it.status == Status.SUCCESS) {
                        Timber.d("auth token : %s", it.data?.token)

                        val prefs = Prefs()
                        prefs.context = this
                        prefs.putUser(Gson().toJson(it.data))

                        startActivity(Intent(this, MainActivity::class.java))

                        hud!!.dismiss()
                    }

                    if (it.status == Status.ERROR) {
                        Timber.d("message : %s", it.message)

                        hud!!.dismiss()
                    }
                })
        } else {
            val snackBar = Snackbar.make(login_view, getString(R.string.connection_not_available), Snackbar.LENGTH_INDEFINITE)
            snackBar.show()
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}
