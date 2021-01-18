package com.yemen.oshopping.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.yemen.oshopping.R
import com.yemen.oshopping.model.Report
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class UpdateReportFragment : Fragment() {
    private lateinit var reportViewModel: OshoppingViewModel
    val reportArg:UpdateReportFragmentArgs by navArgs ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_report, container, false)
        val reportId=reportArg.reportIdArg
        val reportName=reportArg.reportNameArg
        val updateReportTextView = view.findViewById(R.id.update_report_et) as TextView
        updateReportTextView.setText(reportName)
        val updateReportBtn = view.findViewById(R.id.update_report_btn) as Button
        updateReportBtn.setOnClickListener {
            val report=Report(report_id = reportId,report_name =updateReportTextView.text.toString())
            reportViewModel.updateReport(report)
            updateReportTextView.setText("")
            updateReportTextView.clearFocus()
            Navigation.findNavController(view).navigate(R.id.action_updateReportFragment_to_showReportFragment)
        }

        return view
    }


}