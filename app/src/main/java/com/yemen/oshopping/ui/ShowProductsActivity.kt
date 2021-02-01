package com.yemen.oshopping.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yemen.oshopping.R


class ShowProductsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_products)
        supportFragmentManager
            .beginTransaction().
            add(R.id.hidden_products_container,ShowProductsFragment.newInstance(1))
            .commit()
    }
}