package id.langgan.android.seller.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import id.langgan.android.seller.AppExecutors
import id.langgan.android.seller.R
import id.langgan.android.seller.databinding.ActivitySelectProductBinding
import id.langgan.android.seller.di.Injectable
import id.langgan.android.seller.model.Auth
import id.langgan.android.seller.model.Product
import id.langgan.android.seller.ui.adapter.ProductSelectAdapter
import id.langgan.android.seller.utility.Prefs
import id.langgan.android.seller.viewmodel.ProductViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import timber.log.Timber
import javax.inject.Inject

class SelectProductActivity : AppCompatActivity(), HasSupportFragmentInjector, Injectable {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProductViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var binding: ActivitySelectProductBinding

    lateinit var adapter: ProductSelectAdapter

    private var auth: Auth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_product)

        val prefs = Prefs()
        prefs.context = this
        auth = Gson().fromJson(prefs.user, Auth::class.java)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ProductViewModel::class.java)
        viewModel.setAuth(auth?.token)
        viewModel.setStoreId(auth?.stores!![0].id)

        binding.lifecycleOwner = this

        setupRecyclerView()

        getProducts()
    }

    private fun setupRecyclerView() {
        val rvAdapter = ProductSelectAdapter(
            appExecutors = appExecutors
        ) { product -> select(product) }

        binding.rvProducts.adapter = rvAdapter
        adapter = rvAdapter
    }

    private fun getProducts() {
        binding.products = viewModel.products
        viewModel.products.observe(this, Observer { result ->
            adapter.submitList(result?.data)
        })
    }

    private fun select(product: Product) {
        val intent = Intent()
        Timber.d("product $product")
        intent.putExtra("product", Gson().toJson(product))
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}
