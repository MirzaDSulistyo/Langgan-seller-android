package id.langgan.android.seller.ui.activity

import android.content.Context
import android.content.Intent
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
import id.langgan.android.seller.model.Product
import id.langgan.android.seller.ui.adapter.ProductInputAdapter
import id.langgan.android.seller.utility.Prefs
import id.langgan.android.seller.viewmodel.BoxViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import timber.log.Timber
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

    lateinit var adapter: ProductInputAdapter

    private var auth: Auth? = null

    private var products = ArrayList<Product>()

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

        binding.btnAddProduct.setOnClickListener { selectProducts() }

        setupRecyclerView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_PRODUCT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val json = data?.getStringExtra("product")
                val product = Gson().fromJson(json, Product::class.java)
                products.add(product)

                Timber.d("product $json")

                Timber.d("products len : ${products.size}")

                adapter.submitList(products)
            }
        }
    }

    private fun selectProducts() {
        startActivityForResult(Intent(this, SelectProductActivity::class.java), SELECT_PRODUCT_REQUEST_CODE)
    }

    private fun setupRecyclerView() {
        val rvAdapter = ProductInputAdapter(
            appExecutors = appExecutors
        ) { product -> select(product) }

        binding.rvProducts.adapter = rvAdapter
        adapter = rvAdapter

        adapter.submitList(products)
    }

    private fun select(product: Product) {
        //
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    companion object {
        private const val SELECT_PRODUCT_REQUEST_CODE = 101
    }


}
