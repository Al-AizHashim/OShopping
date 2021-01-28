package com.yemen.oshopping.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yemen.oshopping.R
import com.yemen.oshopping.model.ProductReportsDetailsF
import com.yemen.oshopping.viewmodel.OshoppingViewModel

private const val ARG_PARAM1="option"
class ShowProductReportsFragment : Fragment() {
    private lateinit var oshoppingViewModel: OshoppingViewModel
    private lateinit var showProductRecyclerView: RecyclerView
    private var param1:Int? =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        param1 = arguments?.getInt(ARG_PARAM1)
        var arg1=0
        var arg2=0

        if(param1==0){
            arg1=0
            arg2=0
        }
        else if(param1==1){
            arg1=1
            arg2=0
        }
        else if (param1==2)
        {
            arg1=0
            arg2=1
        }
        oshoppingViewModel =
            ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
                    oshoppingViewModel.getProductReportsDetails(listOf(arg1,arg2))
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
        oshoppingViewModel.ProductReportsDetailsLiveData.observe(viewLifecycleOwner,
            Observer { productReports ->
                productReports?.let { reportDetials ->
                    updateUI(reportDetials)
                }
            })
    }

    private fun updateUI(productReportsDetails: List<ProductReportsDetailsF>) {
        showProductRecyclerView.adapter = ShowProductAdapter(productReportsDetails)
    }

    private inner class ShowReportHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var productReportsDetails: ProductReportsDetailsF
        private val productTV = itemView.findViewById(R.id.against_text_view) as TextView
        private val NoOfReportsTV =
            itemView.findViewById(R.id.number_of_report_text_view) as TextView
        private val reportDetailsBTN = itemView.findViewById(R.id.report_details_btn) as Button

        fun bind(productReportsDetails: ProductReportsDetailsF) {
            this.productReportsDetails = productReportsDetails
            productTV.text = productReportsDetails.product_name
            NoOfReportsTV.text = productReportsDetails.number_of_reports.toString()
            reportDetailsBTN.setOnClickListener {
                ShowProductReportsDialog.newInstance(productReportsDetails.product_id,productReportsDetails.product_name,param1).apply {
                    setTargetFragment(this@ShowProductReportsFragment, 0)
                    show(this@ShowProductReportsFragment.requireFragmentManager(), "Input")
                }
            }
        }
    }

    private inner class ShowProductAdapter(private val productReportsDetails: List<ProductReportsDetailsF>) :
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

        override fun getItemCount(): Int = productReportsDetails.size

        override fun onBindViewHolder(holder: ShowReportHolder, position: Int) {
            val productReportsDetails = productReportsDetails[position]
            holder.bind(productReportsDetails)
        }
    }

    companion object {
        fun newInstance(param1 :Int): ShowProductReportsFragment{
            return ShowProductReportsFragment().apply {
                arguments=Bundle().apply {
                    putInt(ARG_PARAM1,param1)
                }
            }
        }
    }
}
