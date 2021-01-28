package com.yemen.oshopping.ui

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

private const val ARG_PARAM1 = "option_id"

class ShowReportsFragment : Fragment() {
    private lateinit var oshoppingViewModel: OshoppingViewModel
    private lateinit var showProductRecyclerView: RecyclerView

    private var param1: Int? = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        param1 = arguments?.getInt(ARG_PARAM1)
        var arg1 = 0
        var arg2 = 0

        if (param1 == 0) {
            arg1 = 0
            arg2 = 0
        } else if (param1 == 1) {
            arg1 = 1
            arg2 = 0
        } else if (param1 == 2) {
            arg1 = 0
            arg2 = 1
        }
        oshoppingViewModel =
            ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        oshoppingViewModel.getReportsDetails(listOf(arg1, arg2))
        Log.d("ATAG", "onCreate: $param1")
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


    override fun onStart() {
        super.onStart()
        oshoppingViewModel.reportsDetailsLiveData.observe(
            viewLifecycleOwner, Observer { reportsDetails ->
                updateUI(reportsDetails)
            })
    }

    private fun updateUI(reportsDetails: List<ReportsDetails>) {
        showProductRecyclerView.adapter = ShowProductAdapter(reportsDetails)
        Log.d("fetchUser", "User fetched successfully ${reportsDetails}")
    }

    private inner class ShowReportHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val againstTextView = itemView.findViewById(R.id.against_text_view) as TextView
        private val NoOfReportsTV =
            itemView.findViewById(R.id.number_of_report_text_view) as TextView
        private val reportDetailsBTN = itemView.findViewById(R.id.report_details_btn) as Button

        private lateinit var reportsDetails: ReportsDetails

  var mainLayout= itemView.findViewById(R.id.main_layout) as ConstraintLayout
        val translateAnimation: Animation = AnimationUtils.loadAnimation(requireContext(),R.anim.translate_anim)
      
        fun bind(reportsDetailsItem: ReportsDetails) {
            reportsDetails = reportsDetailsItem
        fun bind(reportsDetails: ReportsDetails) {
            mainLayout.startAnimation(translateAnimation)
            this.reportsDetails = reportsDetails

            againstTextView.text = reportsDetails.report_against
            NoOfReportsTV.text = reportsDetails.number_of_reports.toString()
            reportDetailsBTN.setOnClickListener {
                ShowReportDetailsDialog.newInstance(
                    reportsDetails.against,
                    reportsDetails.report_against,
                    param1
                ).apply {
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
        fun newInstance(param1: Int): ShowReportsFragment {
            return ShowReportsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
        }
    }
}
