package com.yemen.oshopping.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yemen.oshopping.R
import com.yemen.oshopping.model.PostReportDetails
import com.yemen.oshopping.model.Report
import com.yemen.oshopping.viewmodel.OshoppingViewModel

private const val PARAM1 ="vendor_id"
class ReportsDialog : DialogFragment() {
    private var adapter: ReportAdapter = ReportAdapter(emptyList())
    private lateinit var ReportRecyclerView: RecyclerView
    private lateinit var oshoppingViewModel: OshoppingViewModel
    lateinit var submitBTN: Button
    lateinit var canceltBTN: Button
    lateinit var ignoretBTN: Button
    var lastCheckedRB: RadioButton? = null
    var reportIdViewHodler: Int? = 0
    var user_id :Int=0
    var vendor_id :Int?=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            vendor_id=it?.getInt(PARAM1)
        }
        Log.d("qqTAG", "onCreate: $vendor_id")
        oshoppingViewModel =
            ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        user_id=oshoppingViewModel.getStoredUserId()
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
        submitBTN = view.findViewById(R.id.action_report_button)
        canceltBTN = view.findViewById(R.id.cancel_report_button)
        ignoretBTN = view.findViewById(R.id.ignore_report_button)
        ignoretBTN.visibility=View.GONE
        submitBTN.setOnClickListener {
            if(reportIdViewHodler!=0){
            val postReportDetails = PostReportDetails(reportIdViewHodler,user_id ,vendor_id!!)
            Toast.makeText(
                this@ReportsDialog.context,
                "Done",
                Toast.LENGTH_SHORT
            ).show()
            oshoppingViewModel.pushReportDetails(postReportDetails)}
            dialog?.dismiss()
        }
        canceltBTN.setOnClickListener {
            dialog?.cancel()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oshoppingViewModel.reportItemLiveData.observe(
            viewLifecycleOwner,
            Observer { productDetails ->
                productDetails?.let { reports ->
                    Log.d("FromObserver", "$reports")
                    updateUI(reports)
                }
            })
    }

    private inner class ReportHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var report: Report
        private var reportName: TextView = itemView.findViewById(R.id.task_title)
        var priceRadioGroup: RadioGroup = itemView.findViewById(R.id.report_radio_group)

        fun bind(report: Report) {
            this.report = report
            reportName.text = this.report.report_name
        }
    }

    private inner class ReportAdapter(var reports: List<Report>) :
        RecyclerView.Adapter<ReportHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : ReportHolder {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.report_item, parent, false)
            return ReportHolder(view)
        }

        override fun onBindViewHolder(holder: ReportHolder, position: Int) {
            val report = reports[position]
            holder.priceRadioGroup.setOnCheckedChangeListener { group: RadioGroup, checkedId: Int ->
                var checked_rb = group.findViewById(checkedId) as (RadioButton)
                if (lastCheckedRB != null) {
                    lastCheckedRB?.isChecked = false
                }
                //store the clicked radiobutton
                lastCheckedRB = checked_rb
                reportIdViewHodler = report.report_id
            }
            holder.bind(report)
        }

        override fun getItemCount() = reports.size
    }

    private fun updateUI(reports: List<Report>) {
        adapter.let {
            it.reports = reports
        }
        ReportRecyclerView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1:Int) :ReportsDialog{
            return ReportsDialog().apply {
                arguments=Bundle().apply {
                    putInt(PARAM1,param1)
                }
            }
        }
    }
}




