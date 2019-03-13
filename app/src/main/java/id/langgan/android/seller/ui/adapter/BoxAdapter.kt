package id.langgan.android.seller.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import id.langgan.android.seller.AppExecutors
import id.langgan.android.seller.R
import id.langgan.android.seller.databinding.ContentBoxBinding
import id.langgan.android.seller.model.Box
import id.langgan.android.seller.ui.common.DataBoundListAdapter

class BoxAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val clickCallback: ((Box) -> Unit?)
) : DataBoundListAdapter<Box, ContentBoxBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Box>() {
        override fun areItemsTheSame(oldItem: Box, newItem: Box): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Box, newItem: Box): Boolean {
            return oldItem.price == newItem.price && oldItem.description == newItem.description
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ContentBoxBinding {
        val binding = DataBindingUtil.inflate<ContentBoxBinding>(
            LayoutInflater.from(parent.context),
            R.layout.content_box,
            parent,
            false,
            dataBindingComponent
        )

        binding.cvBox.setOnClickListener {
            binding.box?.let {
                clickCallback.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ContentBoxBinding, item: Box, position: Int) {
        binding.box = item
        binding.position = position

    }
}