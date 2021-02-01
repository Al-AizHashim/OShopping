package com.yemen.oshopping.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yemen.oshopping.R
import com.yemen.oshopping.model.BlockUser
import com.yemen.oshopping.model.HideProduct
import com.yemen.oshopping.model.ProductItem
import com.yemen.oshopping.viewmodel.OshoppingViewModel


private const val ARG_PARAM1 = "hide"

class ShowProductsFragment : Fragment() {
    private lateinit var oshoppingViewModel: OshoppingViewModel
    private lateinit var productRecyclerView: RecyclerView
    var param1: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        param1 = arguments.let { arg ->
            arg?.getInt(ARG_PARAM1)
        }
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        oshoppingViewModel.getProductByHide(1)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.catagory_fragment, container, false)
        productRecyclerView = view.findViewById(R.id.category_recycler_view22)
        productRecyclerView.layoutManager = GridLayoutManager(context, 1)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oshoppingViewModel.productItemLiveDataByHide.observe(
            viewLifecycleOwner,
            Observer { products ->
                productRecyclerView.adapter = ProductAdapter(products)
            })
    }


    private inner class ProductHolder(itemTextView: View) : RecyclerView.ViewHolder(itemTextView),
        View.OnClickListener {
        var mainLayout = itemView.findViewById(R.id.main_layout) as ConstraintLayout
        val translateAnimation: Animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.translate_anim)
        val productNameTV = itemTextView.findViewById(R.id.product_name_tv) as TextView
        val vendorNameTV = itemTextView.findViewById(R.id.vendor_name_tv) as TextView
        var unHideBTN = itemTextView.findViewById(R.id.hide_product_btn) as Button
        lateinit var product: ProductItem
        fun bind(product: ProductItem) {
            mainLayout.startAnimation(translateAnimation)
            this.product = product
            productNameTV.text = product.product_name
            vendorNameTV.text = product.dollar_price.toString()+"$"
                unHideBTN.setOnClickListener {
                    var hideProduct =
                        HideProduct(product.product_id, 0, oshoppingViewModel.getStoredUserId(), 0)
                    oshoppingViewModel.hideProduct(hideProduct)
                    Toast.makeText(
                        this@ShowProductsFragment.context,
                        "product unhided successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    restartActivity()
                }

        }

        private fun restartActivity() {
            val intent = Intent(this@ShowProductsFragment.context, ShowProductsActivity::class.java)
            startActivity(intent)
            if (activity != null) {
                requireActivity().finish()
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent = Intent(itemView.context, ProductDetailsActivity::class.java)
            intent.putExtra("PRODUCTID", product.product_id)
            itemView.context.startActivity(intent)
        }


    }


    private inner class ProductAdapter(private val products: List<ProductItem>)

        : RecyclerView.Adapter<ProductHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ProductHolder {
            val View = LayoutInflater.from(parent.context)
                .inflate(R.layout.product_item, parent, false)
            return ProductHolder(View)
        }

        override fun getItemCount(): Int = products.size

        override fun onBindViewHolder(holder: ProductHolder, position: Int) {
            val category = products[position]
            holder.bind(category)
        }
    }

    companion object {
        fun newInstance(param1: Int): ShowProductsFragment {
            return ShowProductsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
        }
    }
}
