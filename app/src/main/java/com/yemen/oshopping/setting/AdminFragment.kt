package com.yemen.oshopping.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.yemen.oshopping.R

class AdminFragment : Fragment() {

    lateinit var showCategoryTV: TextView
    lateinit var showReportTV: TextView
    lateinit var showReportsDetailsTV: TextView

    lateinit var showProdcutReportsDetailsTV: TextView

    lateinit var close: ImageButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin, container, false)
        showCategoryTV = view.findViewById(R.id.show_category_tv)
        showReportTV = view.findViewById(R.id.show_report_tv)
        showReportsDetailsTV = view.findViewById(R.id.show_report_details_tv)

        showProdcutReportsDetailsTV = view.findViewById(R.id.show_product_report_tv)

        close=view.findViewById(R.id.bt_close)


        showCategoryTV.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_adminFragment_to_showCategoryFragment)
        }
        showReportTV.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_adminFragment_to_showReportFragment)
        }

        showReportsDetailsTV.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_adminFragment_to_showReportsFragment)
        }
        close.setOnClickListener {
            activity?.onBackPressed()
        }

        showProdcutReportsDetailsTV.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_adminFragment_to_showProductReportsFragment)
        }
        return view
    }


}