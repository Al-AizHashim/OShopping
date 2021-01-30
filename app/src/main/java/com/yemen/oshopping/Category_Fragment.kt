package com.yemen.oshopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yemen.oshopping.model.Category
import com.yemen.oshopping.viewmodel.OshoppingViewModel

private const val TAG = "Category"

class Category_Fragment: Fragment()  {
    private lateinit var categoryViewModel: OshoppingViewModel
    private lateinit var categoryRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.catagory_fragment, container, false)
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view22)
        categoryRecyclerView.layoutManager = GridLayoutManager(context, 1)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewModel.categoryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { categorys ->
                Log.d("fetchCategory", "Category fetched successfully ${categorys}")
                categoryRecyclerView.adapter = CategoryAdapter(categorys)

            })
    }

    private inner class CategoryHolder(itemTextView: View)
        : RecyclerView.ViewHolder(itemTextView) {
         val catrgoryImageView = itemTextView.findViewById(R.id.imageView2) as ImageView
        val catrgoryTextView = itemTextView.findViewById(R.id.category) as TextView
        var mainLayout= itemView.findViewById(R.id.main_layout) as ConstraintLayout
        val translateAnimation: Animation = AnimationUtils.loadAnimation(requireContext(),R.anim.translate_anim)
        fun bind(cate: Category){
            Picasso.get().load(MainActivity.LOCAL_HOST_URI+cate.category_image).into(catrgoryImageView)
            catrgoryTextView.text=cate.cat_name
            mainLayout.startAnimation(translateAnimation)
        }
    }

    private inner class CategoryAdapter(private val categorys: List<Category>)

        : RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CategoryHolder {
            val View = LayoutInflater.from(parent.context).inflate(R.layout.category_recycler,parent,false)
            return CategoryHolder(View)
        }
        override fun getItemCount(): Int = categorys.size

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val category = categorys[position]
            holder.bind(category)
        }
    }

    companion object {
        fun newInstance(): Category_Fragment {
            return Category_Fragment()
        }
    }
}
