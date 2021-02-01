package com.yemen.oshopping.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yemen.oshopping.R


class ShowUsersActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var tabViewPager: ViewPager2
    private lateinit var titleTV: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_users_reports)
        titleTV=findViewById(R.id.title_tv)
        titleTV.text = "Users"
        tabLayout = findViewById(R.id.taps)
        tabViewPager = findViewById(R.id.pager)

        tabViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> ShowUsersFragment.newInstance(position)
                    1 -> ShowUsersFragment.newInstance(position)
                    else -> ShowUsersFragment.newInstance(position)
                }
            }

            override fun getItemCount(): Int {
                return 2


            }
        }

        TabLayoutMediator(tabLayout, tabViewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "users "
                    tab.setIcon(R.drawable.ic_baseline_supervisor_account_24)
                }
                1 -> {
                    tab.text = "blocked users"
                    tab.setIcon(R.drawable.ic_baseline_block_26)
                }

                else -> null
            }
        }.attach()
    }
}