package com.yemen.oshopping.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.yemen.oshopping.R
import com.yemen.oshopping.model.Report
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class AddReportFragment : Fragment() {

    lateinit var addReportBtn: Button
    lateinit var addReportEditText: EditText
    lateinit var oshoppingViewModel: OshoppingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oshoppingViewModel= ViewModelProviders.of(this).get(OshoppingViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_add_report, container, false)
        addReportBtn=view.findViewById(R.id.add_report_btn)
        addReportEditText=view.findViewById(R.id.add_report_et)
        addReportBtn.setOnClickListener {
            val report= Report(report_name= addReportEditText.text.toString())
            oshoppingViewModel.pushReport(report)
            addReportEditText.setText("")
            addReportEditText.clearFocus()
            Navigation.findNavController(view)
                .navigate(R.id.action_addReportFragment_to_showReportFragment)

        }
        return view
    }


}