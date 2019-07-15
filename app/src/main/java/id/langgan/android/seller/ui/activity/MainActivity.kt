package id.langgan.android.seller.ui.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amulyakhare.textdrawable.TextDrawable
import com.google.gson.Gson
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.langgan.android.seller.BuildConfig
import id.langgan.android.seller.R
import id.langgan.android.seller.di.Injectable
import id.langgan.android.seller.model.Auth
import id.langgan.android.seller.ui.fragment.ProductTabFragment
import id.langgan.android.seller.ui.fragment.ProfileFragment
import id.langgan.android.seller.utility.ColorGenerator
import id.langgan.android.seller.utility.Helper
import id.langgan.android.seller.utility.Prefs
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, Injectable {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private var currentFragment: Fragment? = null
    private var mIsLargeLayout: Boolean = false
    private var mIsPortrait: Boolean = false

    companion object {
        private const val NAV_MENU_HOME = 1
        private const val NAV_MENU_ORDER = 2
        private const val NAV_MENU_DELIVERY = 3
        private const val NAV_MENU_PRODUCT = 4
        private const val NAV_MENU_REVIEW = 5
        private const val NAV_MENU_STORE = 6
        private const val NAV_MENU_SETUP = 7

        private const val NAV_MENU_PROFILE = 100
        private const val NAV_MENU_LOGOUT = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val prefs = Prefs()
        prefs.context = this

        Timber.d("usernya ${prefs.user}")
        if (prefs.user.isEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            val auth = Gson().fromJson(prefs.user, Auth::class.java)
            Timber.d("user : %s", auth.token)
            Timber.d("user : %s", auth.user?.email)
            Timber.d("user stores : %s", auth.stores?.size)
        }

        // Handle Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.app_name)

        mIsLargeLayout = resources.getBoolean(R.bool.large_layout)
        mIsPortrait = resources.getBoolean(R.bool.portrait_only)
        requestedOrientation = if (mIsPortrait)
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        else
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        val username = "Mirza Sulistyo"
        val email = "mirzadanu@ymail.com"
        val textDrawable = Helper.getFirstTwoChars(username)

        val drawable = TextDrawable.builder().beginConfig()
            .width(100)  // width in px
            .height(100) // height in px
            .endConfig()
            .round().build(textDrawable, ColorGenerator.STORE.getColor(username))

        val profile = ProfileDrawerItem().withName(username).withEmail(email).withIcon(drawable).withIdentifier(NAV_MENU_PROFILE.toLong())

        val headerResult = AccountHeaderBuilder()
            .withActivity(this)
            .withTranslucentStatusBar(true)
            .withSelectionListEnabledForSingleProfile(false)
            .addProfiles(
                profile,
                ProfileSettingDrawerItem().withName(getString(R.string.logout)).withIcon(FontAwesome.Icon.faw_sign_out_alt).withIdentifier(NAV_MENU_LOGOUT.toLong())
            )
            .withOnAccountHeaderListener { _, profile1, _ ->
                when (profile1.identifier.toInt()) {
                    NAV_MENU_LOGOUT -> {
                        doLogout()
                    }

                    NAV_MENU_PROFILE -> {
                        supportActionBar!!.title = getString(R.string.profile)
                        val fragment = ProfileFragment.newInstance()
                        val fragmentManager = supportFragmentManager
                        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()

                        currentFragment = fragment
                    }
                }

                false
            }
            .withSavedInstance(savedInstanceState)
            .build()

        //Create the drawer
        val drawerBuilder = DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withHasStableIds(true)
            .withItemAnimator(AlphaCrossFadeAnimator())
            .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header;

        drawerBuilder.addDrawerItems(
            PrimaryDrawerItem().withName(getString(R.string.home)).withIcon(ContextCompat.getDrawable(this, R.drawable.ic_home)).withIdentifier(NAV_MENU_HOME.toLong()).withSelectable(true),
            PrimaryDrawerItem().withName(getString(R.string.order)).withIcon(ContextCompat.getDrawable(this, R.drawable.ic_dollar_sign)).withIdentifier(NAV_MENU_ORDER.toLong()).withSelectable(true),
            PrimaryDrawerItem().withName(getString(R.string.delivery)).withIcon(ContextCompat.getDrawable(this, R.drawable.ic_truck)).withIdentifier(NAV_MENU_DELIVERY.toLong()).withSelectable(true),
            PrimaryDrawerItem().withName(getString(R.string.product)).withIcon(ContextCompat.getDrawable(this, R.drawable.ic_package)).withIdentifier(NAV_MENU_PRODUCT.toLong()).withSelectable(true),
            PrimaryDrawerItem().withName(getString(R.string.reviews)).withIcon(ContextCompat.getDrawable(this, R.drawable.ic_message_square)).withIdentifier(NAV_MENU_REVIEW.toLong()).withSelectable(true),
            PrimaryDrawerItem().withName(getString(R.string.store)).withIcon(ContextCompat.getDrawable(this, R.drawable.ic_map_pin)).withIdentifier(NAV_MENU_STORE.toLong()).withSelectable(true),
            PrimaryDrawerItem().withName(getString(R.string.settings)).withIcon(ContextCompat.getDrawable(this, R.drawable.ic_settings)).withIdentifier(NAV_MENU_SETUP.toLong()).withSelectable(true)
        )

        drawerBuilder.withOnDrawerItemClickListener { _, _, drawerItem ->
            if (drawerItem != null) {
                when (drawerItem.identifier.toInt()) {
                    NAV_MENU_HOME -> {
                        supportActionBar!!.title = getString(R.string.home)
                        val fragment = ProfileFragment.newInstance()
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()

                        currentFragment = fragment
                    }
                    NAV_MENU_ORDER -> {
                        supportActionBar!!.title = getString(R.string.order)
                        val fragment = ProfileFragment.newInstance()
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()

                        currentFragment = fragment
                    }
                    NAV_MENU_DELIVERY -> {
                        supportActionBar!!.title = getString(R.string.delivery)
                        val fragment = ProfileFragment.newInstance()
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()

                        currentFragment = fragment
                    }
                    NAV_MENU_PRODUCT -> {
                        supportActionBar!!.title = getString(R.string.product)
                        val fragment = ProductTabFragment.newInstance()
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()

                        currentFragment = fragment
                    }
                    NAV_MENU_REVIEW -> {
                        supportActionBar!!.title = getString(R.string.reviews)
                        val fragment = ProfileFragment.newInstance()
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()

                        currentFragment = fragment
                    }
                    NAV_MENU_STORE -> {
                        supportActionBar!!.title = getString(R.string.store)
                        val fragment = ProfileFragment.newInstance()
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()

                        currentFragment = fragment
                    }
                    NAV_MENU_SETUP -> {
                        supportActionBar!!.title = getString(R.string.settings)
                    }
                }
            }
            false
        }
            .withSavedInstance(savedInstanceState)
            .withShowDrawerOnFirstLaunch(true)
            .build()

        val fragment = ProfileFragment.newInstance()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()

        currentFragment = fragment

    }

    private fun doLogout() {
        val prefs = Prefs()
        prefs.context = this
        prefs.putUser("")

        startActivity(Intent(baseContext, LoginActivity::class.java))
        finish()
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}
