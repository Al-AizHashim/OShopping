package com.yemen.oshopping.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.yemen.oshopping.R
import com.yemen.oshopping.model.Report
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class ShowReportFragment : Fragment() {
    private lateinit var reportViewModel: OshoppingViewModel
    private lateinit var noDataImageView: ImageView
    private lateinit var noDataTextView: TextView
    lateinit var fab: FloatingActionButton
    private lateinit var reportRecyclerView: RecyclerView
    lateinit var closeBtn: ImageButton
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
        closeBtn=view.findViewById(R.id.bt_close)
        fab.setOnClickListener {

            Navigation.findNavController(view)
                .navigate(R.id.action_showReportFragment_to_addReportFragment)
        }
        closeBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()

    }
    fun updateUi(){
        reportViewModel.reportItemLiveData.observe(
            viewLifecycleOwner,
            Observer { listOfReports ->
                Log.d("fetchReport", "Reports fetched successfully ${listOfReports}")
                reportRecyclerView.adapter = ReportAdapter(listOfReports)


            })

    }

    private fun restoreDeletedData(view: View, deletedItem: Report) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.report_name}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            reportViewModel.pushReport(deletedItem)
            updateUi()
        }
        snackBar.show()
    }

    private inner class ReportHolder(itemTextView: View)
        : RecyclerView.ViewHolder(itemTextView),View.OnClickListener {
        init {
            itemView.setOnClickListener(this)

        }
        lateinit var reportIns:Report
        var mainLayout= itemView.findViewById(R.id.main_layout) as ConstraintLayout
        val translateAnimation: Animation = AnimationUtils.loadAnimation(requireContext(),R.anim.translate_anim)

        val reportTextView = itemTextView.findViewById(R.id.report_text_view) as TextView
        val reportDeleteBtn=itemTextView.findViewById(R.id.delete_image_view) as ImageView

        fun bind(report: Report){
            reportIns=report
            mainLayout.startAnimation(translateAnimation)
            reportTextView.text=report.report_name
            reportDeleteBtn.setOnClickListener {

                reportViewModel.deleteReport(report)
                updateUi()
                restoreDeletedData(ReportHolder(requireView()).itemView,report)

            }
        }

        override fun onClick(view: View?) {
            if (view != null) {
                val action= reportIns.report_id?.let {
                    ShowReportFragmentDirections
                        .actionShowReportFragmentToUpdateReportFragment(reportIdArg = it, reportNameArg = reportIns.report_name)
                }
                if (action != null) {
                    Navigation.findNavController(view)
                        .navigate(action)
                }
            }
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