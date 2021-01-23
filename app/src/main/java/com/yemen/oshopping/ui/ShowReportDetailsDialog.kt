package com.yemen.oshopping.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yemen.oshopping.R
import com.yemen.oshopping.model.ReportDetails
import com.yemen.oshopping.model.User
import com.yemen.oshopping.viewmodel.OshoppingViewModel


private const val ARG_PARAM1 = "user_id"
private const val ARG_PARAM2 = "user_name"

class ShowReportDetailsDialog : DialogFragment() {
    private var adapter: ReportAdapter = ReportAdapter(emptyList())
    private lateinit var ReportRecyclerView: RecyclerView
    private lateinit var oshoppingViewModel: OshoppingViewModel
    lateinit var blockBTN: Button
    lateinit var DialogTiTleTV: TextView
    lateinit var canceltBTN: Button
    private var param1: Int = 0
    private var param2: String = "Report Types"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        oshoppingViewModel =
            ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        oshoppingViewModel.getReportDetailsByUserId(param1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.dialog_reports, container, false)
        ReportRecyclerView = view.findViewById(R.id.report_recycler_view)
        ReportRecyclerView.layoutManager = LinearLayoutManager(context)
        ReportRecyclerView.adapter = adapter
        blockBTN = view.findViewById(R.id.submit_report_button)
        canceltBTN = view.findViewById(R.id.cancel_report_button)
        DialogTiTleTV = view.findViewById(R.id.report_dialog_title)
        DialogTiTleTV.text = param2
        blockBTN.setOnClickListener {
            var user= User(user_id = 1,block = 1)
            oshoppingViewModel.BlockUser(user)
            Toast.makeText(this@ShowReportDetailsDialog.context, "submit", Toast.LENGTH_SHORT).show()
            dialog?.dismiss()
        }
        canceltBTN.setOnClickListener {

            dialog?.cancel()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oshoppingViewModel.fetchReportDetailsByUserId.observe(
            viewLifecycleOwner,
            Observer { productDetails ->
                productDetails?.let { reportDetials ->
                    updateUI(reportDetials)
                }
            })
    }

    private inner class ReportHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var reportDetails: ReportDetails
        private var reportType: TextView = itemView.findViewById(R.id.report_type)
        private var reportSender: TextView = itemView.findViewById(R.id.report_sender_text_view)
        private var reportCreatDate: TextView =
            itemView.findViewById(R.id.report_createdAt_text_view)

        fun bind(reportDetails: ReportDetails) {
            this.reportDetails = reportDetails
            reportType.text = this.reportDetails.report_type
            reportSender.text = this.reportDetails.sender_name
            reportCreatDate.text = this.reportDetails.created_at
        }
    }

    private inner class ReportAdapter(var reports: List<ReportDetails>) :
        RecyclerView.Adapter<ReportHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportHolder {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.show_report_item, parent, false)
            return ReportHolder(view)
        }

        override fun onBindViewHolder(holder: ReportHolder, position: Int) {
            val reportDetails = reports[position]
            holder.bind(reportDetails)
        }

        override fun getItemCount() = reports.size
    }


    private fun updateUI(reports: List<ReportDetails>) {
        adapter.let {
            it.reports = reports
        }
        ReportRecyclerView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: String) = ShowReportDetailsDialog().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}




