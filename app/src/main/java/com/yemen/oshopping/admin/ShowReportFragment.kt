package com.yemen.oshopping.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yemen.oshopping.R
import com.yemen.oshopping.model.Report
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class ShowReportFragment : Fragment() {
    private lateinit var reportViewModel: OshoppingViewModel
    private lateinit var noDataImageView: ImageView
    private lateinit var noDataTextView: TextView
    lateinit var fab: FloatingActionButton
    private lateinit var reportRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_show_report, container, false)
        reportRecyclerView = view.findViewById(R.id.report_recycler_view)
        reportRecyclerView.layoutManager = GridLayoutManager(context, 1)
        fab = view.findViewById(R.id.add_report_fab)
        noDataImageView=view.findViewById(R.id.report_no_data_imageView)
        noDataTextView=view.findViewById(R.id.no_data_textView)
        fab.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_showReportFragment_to_addReportFragment)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reportViewModel.reportItemLiveData.observe(
            viewLifecycleOwner,
            Observer { Reports ->
                Log.d("fetchReport", "Reports fetched successfully ${Reports}")
                reportRecyclerView.adapter = ReportAdapter(Reports)

            })
    }

    private class ReportHolder(itemTextView: View)
        : RecyclerView.ViewHolder(itemTextView) {

        val reportTextView = itemTextView.findViewById(R.id.report_text_view) as TextView

        fun bind(report: Report){
            reportTextView.text=report.report_name
        }
    }

    private inner class ReportAdapter(private val reports: List<Report>)

        : RecyclerView.Adapter<ReportHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ReportHolder {
            val View = LayoutInflater.from(parent.context).inflate(R.layout.report_item_list,parent,false)
            return ReportHolder(View)
        }
        override fun getItemCount(): Int{
            val numberOfReports= reports.size
            if (numberOfReports<1){
                noDataTextView.visibility=View.VISIBLE
                noDataImageView.visibility=View.VISIBLE
            }
            return numberOfReports
        }

        override fun onBindViewHolder(holder: ReportHolder, position: Int) {
            val report = reports[position]
            holder.bind(report)
        }
    }



}