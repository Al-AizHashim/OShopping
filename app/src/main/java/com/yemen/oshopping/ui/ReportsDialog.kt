package com.yemen.oshopping.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yemen.oshopping.R
import com.yemen.oshopping.model.Report
import com.yemen.oshopping.viewmodel.OshoppingViewModel

class ReportsDialog : DialogFragment() {
    private var adapter: ReportAdapter = ReportAdapter(emptyList())
    private lateinit var ReportRecyclerView: RecyclerView
    private lateinit var oshoppingViewModel: OshoppingViewModel
    lateinit var submitBTN:Button
    lateinit var canceltBTN:Button


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
        var view = inflater.inflate(R.layout.dialog_reports, container, false)
        ReportRecyclerView = view.findViewById(R.id.report_recycler_view)
        ReportRecyclerView.layoutManager = LinearLayoutManager(context)
        ReportRecyclerView?.adapter = adapter
        submitBTN=view.findViewById(R.id.submit_report_button)
        canceltBTN=view.findViewById(R.id.cancel_report_button)
        submitBTN.setOnClickListener {
            Toast.makeText(this@ReportsDialog.context,"submit",Toast.LENGTH_LONG).show()
        }
        canceltBTN.setOnClickListener {
            Toast.makeText(this@ReportsDialog.context,"submit",Toast.LENGTH_LONG).show()

        }
        return view
    }
    private inner class ReportHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        private lateinit var report: Report
        private var reportName: TextView = itemView.findViewById(R.id.task_title)
        private var reportRBTN: RadioButton = itemView.findViewById(R.id.report_radio_button)
        override fun onClick(v: View?) {
            Toast.makeText(context, "in onclic", Toast.LENGTH_LONG).show()

        }

        fun bind(report: Report) {
            this.report = report
            Log.d("GGG", "bind: ${report.report_name}")
            reportName.text = this.report.report_name
            Log.d("RRR", "bind: ${reportName.text}")
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oshoppingViewModel.reportItemLiveData.observe(
            viewLifecycleOwner,
            Observer { productDetails ->
                productDetails?.let {reports->
                    Log.d("FromObserver", "${reports.toString()}")
                    updateUI(reports)
                }
            })
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
            holder.bind(report)
        }

        override fun getItemCount() = reports.size
    }

    companion object {
        @JvmStatic
        fun newInstance() = ReportsDialog()
    }
    private fun updateUI(reports: List<Report>) {
        adapter.let {
            it.reports = reports
        }
        Log.d("TAAAG", "updateUI: ${adapter.reports}")
        ReportRecyclerView?.adapter = adapter
    }

}




