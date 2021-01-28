package com.yemen.oshopping.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yemen.oshopping.R
import com.yemen.oshopping.model.ReportsDetails
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class ShowReportsFragment : Fragment() {
    private lateinit var oshoppingViewModel: OshoppingViewModel
    private lateinit var showProductRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oshoppingViewModel =
            ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_show_reports, container, false)
        showProductRecyclerView = view.findViewById(R.id.show_reports_recycler_view)
        showProductRecyclerView.layoutManager = GridLayoutManager(context, 1)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oshoppingViewModel.reportsDetailsItemLiveData.observe(
            viewLifecycleOwner, Observer { reportsDetails ->
                updateUI(reportsDetails)
            })
    }

    private fun updateUI(reportsDetails: List<ReportsDetails>) {
        showProductRecyclerView.adapter = ShowProductAdapter(reportsDetails)
    }

    private inner class ShowReportHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var reportsDetails: ReportsDetails
        private val againstTextView = itemView.findViewById(R.id.against_text_view) as TextView
        private val NoOfReportsTV = itemView.findViewById(R.id.number_of_report_text_view) as TextView
        private val reportDetailsBTN = itemView.findViewById(R.id.report_details_btn) as Button
        var mainLayout= itemView.findViewById(R.id.main_layout) as ConstraintLayout
        val translateAnimation: Animation = AnimationUtils.loadAnimation(requireContext(),R.anim.translate_anim)
        fun bind(reportsDetails: ReportsDetails) {
            mainLayout.startAnimation(translateAnimation)
            this.reportsDetails = reportsDetails
            againstTextView.text = reportsDetails.report_against
            NoOfReportsTV.text = reportsDetails.number_of_reports.toString()
            reportDetailsBTN.setOnClickListener {
                ShowReportDetailsDialog.newInstance(reportsDetails.against,reportsDetails.report_against).apply {
                    setTargetFragment(this@ShowReportsFragment, 0)
                    show(this@ShowReportsFragment.requireFragmentManager(), "Input")
                  }
            }
        }
    }

    private inner class ShowProductAdapter(private val reportsDetails: List<ReportsDetails>) :
        RecyclerView.Adapter<ShowReportHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ShowReportHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.show_report_list_item, parent, false)
            return ShowReportHolder(view)
        }

        override fun getItemCount(): Int = reportsDetails.size

        override fun onBindViewHolder(holder: ShowReportHolder, position: Int) {
            val reportsDetails = reportsDetails[position]
            holder.bind(reportsDetails)
        }
    }

    companion object {
        fun newInstance(): ShowReportsFragment {
            return ShowReportsFragment()
        }
    }
}
