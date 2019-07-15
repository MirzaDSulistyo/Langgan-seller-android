package id.langgan.android.seller.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import id.langgan.android.seller.AppExecutors
import id.langgan.android.seller.R
import id.langgan.android.seller.databinding.ContentSelectProductBinding
import id.langgan.android.seller.model.Product
import id.langgan.android.seller.ui.common.DataBoundListAdapter

class ProductSelectAdapter(
    appExecutors: AppExecutors,
    private val clickCallback: ((Product) -> Unit?)
) : DataBoundListAdapter<Product, ContentSelectProductBinding>(
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

    override fun createBinding(parent: ViewGroup): ContentSelectProductBinding {
        val binding = DataBindingUtil.inflate<ContentSelectProductBinding>(
            LayoutInflater.from(parent.context),
            R.layout.content_select_product,
            parent,
            false
        )

        binding.cvProduct.setOnClickListener {
            binding.product?.let {
                clickCallback.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ContentSelectProductBinding, item: Product, position: Int) {
        binding.product = item
        binding.position = position
    }
}