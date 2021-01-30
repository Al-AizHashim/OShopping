package com.yemen.oshopping.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.yemen.oshopping.R
import com.yemen.oshopping.model.ProductReportDetails
import com.yemen.oshopping.viewmodel.OshoppingViewModel

private const val ARG_PARAM1 = "product_id"
class ProductReportsDialog : DialogFragment() {
    private lateinit var oshoppingViewModel: OshoppingViewModel
    lateinit var submitBTN: Button
    lateinit var canceltBTN: Button
    private lateinit var radioGroup: RadioGroup
    private  var radioButton: RadioButton? =null
    private  var productId :Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getInt(ARG_PARAM1)
        }
        oshoppingViewModel =
            ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.dialog_product_report, container, false)
        submitBTN = view.findViewById(R.id.submit_report_button)
        canceltBTN = view.findViewById(R.id.cancel_report_button)
        radioGroup = view.findViewById(R.id.product_report_radio_group)
        var user_id = oshoppingViewModel.getStoredUserId()
        submitBTN.setOnClickListener {
            // get selected radio button from radioGroup
            val selectedId: Int = radioGroup.checkedRadioButtonId
            // find the radiobutton by returned id
            radioButton = view.findViewById(selectedId)
            if (radioButton != null && productId!=0) {
                var x = 0
                if (radioButton?.text == getString(R.string.produt_raidoBtn_first)) {
                    x = 1
                } else if (radioButton?.text == getString(R.string.produt_raidoBtn_second)) {
                    x = 2
                } else if (radioButton?.text == getString(R.string.produt_raidoBtn_third)) {
                    x = 3
                }
                val productReportDetails =
                    ProductReportDetails(product_id = this.productId, product_r_id = x, sender_id =user_id )
                oshoppingViewModel.pushProductReportDetails(productReportDetails)
                Toast.makeText(
                    this@ProductReportsDialog.context,
                    "Done",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else{
                Toast.makeText(
                    this@ProductReportsDialog.context,
                    "Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialog?.dismiss()
        }

        canceltBTN.setOnClickListener {
            dialog?.cancel()
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1:Int) =
            ProductReportsDialog().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}




