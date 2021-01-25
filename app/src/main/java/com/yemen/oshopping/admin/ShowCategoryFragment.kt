package com.yemen.oshopping.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.yemen.oshopping.R
import com.yemen.oshopping.model.Category
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class ShowCategoryFragment : Fragment() {
    var url: String = "http://192.168.1.108/oshopping_api/"
    private lateinit var categoryViewModel: OshoppingViewModel
    private lateinit var noDataImageView: ImageView
    private lateinit var noDataTextView: TextView
    lateinit var fab: FloatingActionButton
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var delete_button: ImageView
    lateinit var close: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_show_category, container, false)
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view)
        categoryRecyclerView.layoutManager = GridLayoutManager(context, 1)
        fab = view.findViewById(R.id.add_category_fab)
        noDataImageView=view.findViewById(R.id.no_data_imageView)
        noDataTextView=view.findViewById(R.id.no_data_textView)
        close=view.findViewById(R.id.bt_close)
        fab.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_showCategoryFragment_to_addCategoryFragment)
        }
        close.setOnClickListener {
            activity?.onBackPressed()
        }


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()
    }

    fun updateUi(){
        categoryViewModel.categoryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { categorys ->
                Log.d("fetchCategory", "Category fetched successfully ${categorys.size}")
                categoryRecyclerView.adapter = CategoryAdapter(categorys)

            })
    }

    private inner class CategoryHolder(itemTextView: View)
        : RecyclerView.ViewHolder(itemTextView) ,View.OnClickListener {
        init {
            itemView.setOnClickListener(this)

        }
        lateinit var catergoryInstance: Category
        val catrgoryTextView = itemTextView.findViewById(R.id.category) as TextView
        val catrgoryDeleteBtn = itemTextView.findViewById(R.id.delete_category_btn) as ImageView
        fun bind(cate: Category){
            catergoryInstance=cate
            catrgoryTextView.text=cate.cat_name
            catrgoryDeleteBtn.setOnClickListener {

                categoryViewModel.deleteCategory(cate)
                updateUi()
                restoreDeletedData(CategoryHolder(requireView()).itemView,cate)

            }
        }


        override fun onClick(view: View?) {
            if (view != null) {
                val action= catergoryInstance.cat_id?.let {
                    ShowCategoryFragmentDirections
                        .actionShowCategoryFragmentToUpdateCategoryFragment(
                            categoryIdArg = it,
                            categoryNameArg = catergoryInstance.cat_name)
                }
                if (action != null) {
                    Navigation.findNavController(view)
                        .navigate(action)
                }
            }
        }
    }

    private inner class CategoryAdapter(private val categorys: List<Category>)

        : RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CategoryHolder {
            val View = LayoutInflater.from(parent.context).inflate(R.layout.admin_list_category,parent,false)
            return CategoryHolder(View)
        }
        override fun getItemCount(): Int{
            val numberOfCategories= categorys.size
            if (numberOfCategories<1){
                noDataTextView.visibility=View.VISIBLE
                noDataImageView.visibility=View.VISIBLE
            }
            return numberOfCategories
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val category = categorys[position]
            holder.bind(category)
        }
    }

    private fun restoreDeletedData(view: View, deletedItem: Category) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.cat_name}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            categoryViewModel.pushcat(deletedItem)
            updateUi()
        }
        snackBar.show()
    }



}


