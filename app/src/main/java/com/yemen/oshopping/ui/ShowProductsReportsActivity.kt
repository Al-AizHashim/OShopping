package com.yemen.oshopping.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yemen.oshopping.R

class ShowProductsReportsActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var tabViewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_products_reports)
        tabLayout = findViewById(R.id.taps)
        tabViewPager = findViewById(R.id.pager)

        tabViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> ShowProductReportsFragment.newInstance(position)
                    1 -> ShowProductReportsFragment.newInstance(position)
                    2 -> ShowProductReportsFragment.newInstance(position)
                    else -> ShowProductReportsFragment.newInstance(0)
                }
            }

            override fun getItemCount(): Int {
                return 3
            }
        }

        TabLayoutMediator(tabLayout, tabViewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Reports"
                    tab.setIcon(R.drawable.ic_baseline_report_problem_24)
                }
                1 -> {
                    tab.text = "block list"
                    tab.setIcon(R.drawable.ic_baseline_block_24)
                }
                2 -> {
                    tab.text = "Checked"
                    tab.setIcon(R.drawable.ic_baseline_check_circle_24)
                }

                else -> null
            }

        }.attach()
    }
}