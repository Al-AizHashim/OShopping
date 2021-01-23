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
import com.yemen.oshopping.model.Category
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class UpdateCategoryFragment : Fragment() {

    private lateinit var categoryViewModel: OshoppingViewModel
    val categoryArgs: UpdateCategoryFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_update_category, container, false)
        val categoryId = categoryArgs.categoryIdArg
        val categoryName = categoryArgs.categoryNameArg
        val updateCategoryTextView = view.findViewById(R.id.update_cat_editText) as TextView
        updateCategoryTextView.setText(categoryName)
        val updateCategoryBtn = view.findViewById(R.id.update_cat_btn) as Button
        updateCategoryBtn.setOnClickListener {
            val category =
                Category(cat_id = categoryId, cat_name = updateCategoryTextView.text.toString())
            categoryViewModel.
            updateCategory(category)
            updateCategoryTextView.setText("")
            updateCategoryTextView.clearFocus()
            Navigation.findNavController(view)
                .navigate(R.id.action_updateCategoryFragment_to_showCategoryFragment)
        }
        return view
    }

}