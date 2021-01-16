package com.yemen.oshopping.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yemen.oshopping.R
import com.yemen.oshopping.model.Category
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class AddCategoryFragment : Fragment() {

    lateinit var addCategoryBtn: Button
    lateinit var updateCategoryBtn: Button
    lateinit var addCategoryEditText: EditText
    lateinit var oshoppingViewModel: OshoppingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oshoppingViewModel= ViewModelProviders.of(this).get(OshoppingViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_add_category, container, false)
        addCategoryBtn=view.findViewById(R.id.add_cat_btn)
        updateCategoryBtn=view.findViewById(R.id.update_cat_btn)
        addCategoryEditText=view.findViewById(R.id.add_cat_editText)
        addCategoryBtn.setOnClickListener {
            val cat= Category(cat_name=addCategoryEditText.text.toString())
            oshoppingViewModel.pushcat(cat)

        }
        updateCategoryBtn.setOnClickListener {

            oshoppingViewModel.loadProductByCategory(1)
            oshoppingViewModel.productItemLiveDataByCategory.observe(
                viewLifecycleOwner,
                Observer { DataByCategory ->
                    DataByCategory?.let {
                        Log.d("FromObserver", it.toString())


                    }
                })
        }
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddCategoryFragment()
    }
}