package id.langgan.android.seller.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import id.langgan.android.seller.R
import id.langgan.android.seller.ui.adapter.TabAdapter

class ProductTabFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tabs, container, false)

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = view.findViewById<ViewPager>(R.id.view_pager_tabs)
        val adapter = TabAdapter(fragmentManager!!)

        adapter.addFragment(ProductFragment(), getString(R.string.product))
        adapter.addFragment(BoxFragment(), getString(R.string.box))

        // setup viewpager
        viewPager.adapter = adapter

        // setup TabLayout
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    companion object {
        fun newInstance(): ProductTabFragment {
            return ProductTabFragment()
        }
    }

}