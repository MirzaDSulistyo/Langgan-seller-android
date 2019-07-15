package id.langgan.android.seller.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import id.langgan.android.seller.AppExecutors
import id.langgan.android.seller.R
import id.langgan.android.seller.databinding.ContentInputProductBinding
import id.langgan.android.seller.model.Product
import id.langgan.android.seller.ui.common.DataBoundListAdapter

class ProductInputAdapter(
    appExecutors: AppExecutors,
    private val clickCallback: ((Product) -> Unit?)
) : DataBoundListAdapter<Product, ContentInputProductBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.price == newItem.price && oldItem.brand == newItem.brand
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ContentInputProductBinding {
        val binding = DataBindingUtil.inflate<ContentInputProductBinding>(
            LayoutInflater.from(parent.context),
            R.layout.content_input_product,
            parent,
            false
        )

        binding.cvInputProduct.setOnClickListener {
            binding.product?.let {
                clickCallback.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ContentInputProductBinding, item: Product, position: Int) {
        binding.product = item
        binding.position = position
    }
}