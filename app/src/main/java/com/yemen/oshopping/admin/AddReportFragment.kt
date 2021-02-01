package com.yemen.oshopping.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout
import com.yemen.oshopping.R
import com.yemen.oshopping.model.Report
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class AddReportFragment : Fragment() {

    lateinit var addReportBtn: Button
    lateinit var addReportEditText: EditText
    lateinit var oshoppingViewModel: OshoppingViewModel
    lateinit var addReportBtn2: ImageButton
    lateinit var closeBtn: ImageButton

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
        addReportBtn2=view.findViewById(R.id.add_report_btn2)
        closeBtn=view.findViewById(R.id.bt_close)
        addReportBtn.setOnClickListener {
            addReport()

        }
        addReportBtn2.setOnClickListener {
            addReport()

        }
        closeBtn.setOnClickListener {
            activity?.onBackPressed()
        }
        return view
    }

    private fun addReport(){
        val report= Report(report_name= addReportEditText.text.toString())
        oshoppingViewModel.pushReport(report)
        addReportEditText.setText("")
        addReportEditText.clearFocus()
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_addReportFragment_to_showReportFragment)
        }
    }


}