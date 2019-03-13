package id.langgan.android.seller.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import id.langgan.android.seller.AppExecutors
import id.langgan.android.seller.di.Injectable
import id.langgan.android.seller.viewmodel.ProductViewModel
import id.langgan.android.seller.data.binding.FragmentDataBindingComponent
import id.langgan.android.seller.databinding.FragmentProductBinding
import id.langgan.android.seller.model.Auth
import id.langgan.android.seller.ui.adapter.ProductAdapter
import id.langgan.android.seller.utility.autoCleared
import id.langgan.android.seller.utility.Prefs
import id.langgan.android.seller.R
import id.langgan.android.seller.model.Product
import id.langgan.android.seller.ui.activity.FormProductActivity
import kotlinx.android.synthetic.main.fragment_product.*
import org.jetbrains.anko.startActivity
import timber.log.Timber
import javax.inject.Inject

class ProductFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ProductViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    var binding by autoCleared<FragmentProductBinding>()

    private var adapter by autoCleared<ProductAdapter>()

    private var auth: Auth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = Prefs()
        prefs.context = context
        auth = Gson().fromJson(prefs.user, Auth::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false, dataBindingComponent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ProductViewModel::class.java)
        viewModel.setAuth(auth?.token)
        viewModel.setStoreId(auth?.stores!![0].id)

        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()

        val rvAdapter = ProductAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors
        ) {  product -> details(product) }

        binding.rvProducts.adapter = rvAdapter
        adapter = rvAdapter

        btn_add_product.setOnClickListener {
            activity?.startActivity<FormProductActivity>()
        }
    }

    private fun initRecyclerView() {
        refresh_products.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        refresh_products.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.products = viewModel.products
        viewModel.products.observe(viewLifecycleOwner, Observer { result ->
            Timber.d("status : %s", result.status)
            Timber.d("message : %s", result.message)
            Timber.d("message : ${result.data?.size}")
            adapter.submitList(result?.data)
            refresh_products.isRefreshing = false
        })
    }

    private fun details(product: Product) {
        Timber.d("product ${product.name}")
    }

    companion object {

        fun newInstance(): ProductFragment {
            return ProductFragment()
        }
    }

}