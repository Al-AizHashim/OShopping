package com.yemen.oshopping.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout
import com.yemen.oshopping.R
import com.yemen.oshopping.model.Category
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import kotlinx.android.synthetic.main.fragment_add_category.*


class AddCategoryFragment : Fragment() {

    lateinit var addCategoryBtn: Button
    lateinit var addCategoryBtn2: ImageButton
    lateinit var addCategoryEditText: TextInputLayout
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
        addCategoryBtn2=view.findViewById(R.id.add_cat_btn2)
        addCategoryEditText=view.findViewById(R.id.add_cat_editText)
        addCategoryBtn.setOnClickListener {
            addCategory()
        }
        addCategoryBtn2.setOnClickListener {
            addCategory()
        }

        return view
    }
    fun addCategory(){
        val cat= Category(cat_name= addCategoryEditText.getEditText()?.getText().toString().trim())
        oshoppingViewModel.pushcat(cat)
        addCategoryEditText.clearFocus()
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_addCategoryFragment_to_showCategoryFragment)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddCategoryFragment()
    }
}