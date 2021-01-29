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
import com.yemen.oshopping.model.*
import com.yemen.oshopping.viewmodel.OshoppingViewModel


private const val ARG_PARAM1 = "user_id"
private const val ARG_PARAM2 = "user_name"
private const val ARG_PARAM3 = "option"

class ShowProductReportsDialog : DialogFragment() {
    private var adapter: ReportAdapter = ReportAdapter(emptyList())
    private lateinit var ReportRecyclerView: RecyclerView
    private lateinit var oshoppingViewModel: OshoppingViewModel
    lateinit var hideBTN: Button
    lateinit var DialogTiTleTV: TextView
    lateinit var canceltBTN: Button
    lateinit var ignoreBTN: Button
    private var param1: Int = 0
    private var param2: String =""
    private var param3: Int =0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getInt(ARG_PARAM3)


        }
        oshoppingViewModel =
            ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        oshoppingViewModel.getProductReportByReportId(param1)
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
        hideBTN = view.findViewById(R.id.action_report_button)
        canceltBTN = view.findViewById(R.id.cancel_report_button)
        ignoreBTN = view.findViewById(R.id.ignore_report_button)
        DialogTiTleTV = view.findViewById(R.id.report_dialog_title)
        DialogTiTleTV.text = param2
        if (param3==0){
            hideBTN.text = "HIDE PRODUCT"
            ignoreBTN.text="Checked"
        }
        else if (param3==1)
        {
            hideBTN.text = "UNHIDE PRODUCT"
            ignoreBTN.visibility=View.GONE

        }
        else if (param3==2)
        {
            hideBTN.text = "HIDE PRODUCT"
            ignoreBTN.text="UNCHECKED"

        }
        hideBTN.setOnClickListener {
            if (param3==0){
                var hideProduct = HideProduct(product_id = param1, hide = 1, user_id = 2,checked = 0)
                oshoppingViewModel.hideProduct(hideProduct)
            }
            else if (param3==1)
            {
                var hideProduct = HideProduct(product_id = param1, hide = 0, user_id = 2,checked = 0)
                oshoppingViewModel.hideProduct(hideProduct)
            }
            else if (param3==2) {
                var hideProduct = HideProduct(product_id = param1, hide = 1, user_id = 2,checked = 0)
                oshoppingViewModel.hideProduct(hideProduct)
            }
            Toast.makeText(this@ShowProductReportsDialog.context, "Done", Toast.LENGTH_SHORT).show()
            restartActivity()

            dialog?.dismiss()
        }
        ignoreBTN.setOnClickListener{
            if (param3==0){
                var hideProduct = HideProduct(product_id = param1, hide = 0, user_id = 2,checked = 1)
                oshoppingViewModel.hideProduct(hideProduct)
                Toast.makeText(this@ShowProductReportsDialog.context, "Done", Toast.LENGTH_SHORT).show()
            }
            else if (param3==1)
            {
            }
            else if (param3==2) {
                var hideProduct = HideProduct(product_id = param1, hide = 0, user_id = 2,checked = 0)
                oshoppingViewModel.hideProduct(hideProduct)
                Toast.makeText(this@ShowProductReportsDialog.context, "Done", Toast.LENGTH_SHORT).show()
            }
            restartActivity()
            dialog?.dismiss()
        }
        canceltBTN.setOnClickListener {

            dialog?.cancel()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oshoppingViewModel.fetchProductReportLiveDataByProductId.observe(
            viewLifecycleOwner,
            Observer { productDetails ->
                productDetails?.let { reportDetials ->
                    updateUI(reportDetials)
                }
            })
    }
    private fun restartActivity() {
        val intent = Intent(this@ShowProductReportsDialog.context, ShowUsersReportsActivity::class.java)
        startActivity(intent)
        if (activity != null) {
            requireActivity().finish()
        }
    }

    private inner class ReportHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var productReportDetails: ProductReportDetailsF
        private var reportType: TextView = itemView.findViewById(R.id.p_report_type)
        private var reportSender: TextView = itemView.findViewById(R.id.p_report_sender_text_view)
        private var productvendor: TextView = itemView.findViewById(R.id.p_report_vendor_text_view)
        private var reportCreatDate: TextView = itemView.findViewById(R.id.p_report_createdAt_text_view)

        fun bind(productReportDetails: ProductReportDetailsF) {
            this.productReportDetails = productReportDetails
            reportType.text = this.productReportDetails.report_type
            reportSender.text = this.productReportDetails.sender_name
            productvendor.text = this.productReportDetails.vendor_name
            reportCreatDate.text = this.productReportDetails.created_at

        }
    }

    private inner class ReportAdapter(var productReport: List<ProductReportDetailsF>) :
        RecyclerView.Adapter<ReportHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportHolder {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.product_report_list_item, parent, false)
            return ReportHolder(view)
        }

        override fun onBindViewHolder(holder: ReportHolder, position: Int) {
            val productReportDetails = productReport[position]
            holder.bind(productReportDetails)
        }

        override fun getItemCount() = productReport.size
    }


    private fun updateUI(productReport: List<ProductReportDetailsF>) {
        adapter.let {
            it.productReport = productReport
        }
        ReportRecyclerView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int,param2 :String,param3 :Int?) = ShowProductReportsDialog().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
                putInt(ARG_PARAM3, param3!!)
            }
        }
    }
}




