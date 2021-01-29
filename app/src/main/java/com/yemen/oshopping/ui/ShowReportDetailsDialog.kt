package com.yemen.oshopping.ui

import android.content.Intent
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
import com.yemen.oshopping.model.BlockUser
import com.yemen.oshopping.model.ReportDetails
import com.yemen.oshopping.viewmodel.OshoppingViewModel


private const val ARG_PARAM1 = "user_id"
private const val ARG_PARAM2 = "user_name"
private const val ARG_PARAM3 = "option_id"

class ShowReportDetailsDialog : DialogFragment() {
    private var adapter: ReportAdapter = ReportAdapter(emptyList())
    private lateinit var ReportRecyclerView: RecyclerView
    private lateinit var oshoppingViewModel: OshoppingViewModel
    lateinit var blockBTN: Button
    lateinit var ignoreBTN: Button
    lateinit var DialogTiTleTV: TextView
    lateinit var canceltBTN: Button
    private var param1: Int = 0
    private var param3: Int = 0
    private var param2: String = "Report Types"
    private lateinit var reportDetailsItem: ReportDetails


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getInt(ARG_PARAM3)
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
        blockBTN = view.findViewById(R.id.action_report_button)
        canceltBTN = view.findViewById(R.id.cancel_report_button)
        ignoreBTN = view.findViewById(R.id.ignore_report_button)
        DialogTiTleTV = view.findViewById(R.id.report_dialog_title)
        DialogTiTleTV.text = param2
        if (param3 == 0) {
            blockBTN.text = "BLOCK UESR"
            ignoreBTN.text = "Checked"
        } else if (param3 == 1) {
            blockBTN.text = "UNBLOCK UESR"
            ignoreBTN.visibility = View.GONE

        } else if (param3 == 2) {
            blockBTN.text = "BLOCK UESR"
            ignoreBTN.text = "UNCHECKED"

        }
        blockBTN.setOnClickListener {
            if (param3 == 0) {
                var blockUser = BlockUser(user_id = param1, block = 1, admin_id = 2, checked = 0)
                oshoppingViewModel.blockUser(blockUser)
            } else if (param3 == 1) {
                var blockUser = BlockUser(user_id = param1, block = 0, admin_id = 2, checked = 0)
                oshoppingViewModel.blockUser(blockUser)
            } else if (param3 == 2) {
                var blockUser = BlockUser(user_id = param1, block = 1, admin_id = 2, checked = 0)
                oshoppingViewModel.blockUser(blockUser)
            }
            Toast.makeText(this@ShowReportDetailsDialog.context, "Done", Toast.LENGTH_SHORT).show()
            restartActivity()
            dialog?.dismiss()
        }
        ignoreBTN.setOnClickListener {
            if (param3 == 0) {
                var blockUser = BlockUser(user_id = param1, block = 0, admin_id = 2, checked = 1)
                oshoppingViewModel.blockUser(blockUser)
            } else if (param3 == 1) {
            } else if (param3 == 2) {
                var blockUser = BlockUser(user_id = param1, block = 0, admin_id = 2, checked = 0)
                oshoppingViewModel.blockUser(blockUser)
            }
            restartActivity()
            dialog?.dismiss()
        }
        canceltBTN.setOnClickListener {
            dialog?.cancel()
        }
        return view
    }
    private fun restartActivity() {
        val intent = Intent(this@ShowReportDetailsDialog.context, ShowUsersReportsActivity::class.java)
        startActivity(intent)
        if (activity != null) {
            requireActivity().finish()
        }
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

    inner class ReportHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var reportType: TextView = itemView.findViewById(R.id.report_type)
        private var reportSender: TextView = itemView.findViewById(R.id.report_sender_text_view)
        private var reportCreatDate: TextView =
            itemView.findViewById(R.id.report_createdAt_text_view)

        fun bind(reportDetailsArg: ReportDetails) {
            reportDetailsItem = reportDetailsArg
            reportType.text = reportDetailsItem.report_type
            reportSender.text = reportDetailsItem.sender_name
            reportCreatDate.text = reportDetailsItem.created_at
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
        fun newInstance(param1: Int, param2: String, param3: Int?) =
            ShowReportDetailsDialog().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putInt(ARG_PARAM3, param3!!)
                }
            }
    }
}




