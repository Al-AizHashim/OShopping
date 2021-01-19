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
import androidx.navigation.Navigation
import com.yemen.oshopping.R
import com.yemen.oshopping.model.Category
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class AddCategoryFragment : Fragment() {

    lateinit var addCategoryBtn: Button
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
        addCategoryEditText=view.findViewById(R.id.add_cat_editText)
        addCategoryBtn.setOnClickListener {
            val cat= Category(cat_name=addCategoryEditText.text.toString())
            oshoppingViewModel.pushcat(cat)
            addCategoryEditText.setText("")
            addCategoryEditText.clearFocus()
            Navigation.findNavController(view)
                .navigate(R.id.action_addCategoryFragment_to_showCategoryFragment)


        }

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddCategoryFragment()
    }
}