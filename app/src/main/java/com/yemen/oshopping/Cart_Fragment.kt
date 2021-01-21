package com.yemen.oshopping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yemen.oshopping.model.Cart
import com.yemen.oshopping.model.Category
import com.yemen.oshopping.model.ProductItem
import com.yemen.oshopping.viewmodel.OshoppingViewModel
import kotlinx.android.synthetic.main.fragment_cart.*

private const val TAG = "Category"

class Cart_Fragment: Fragment() {
    var url: String = "http://192.168.1.108/oshopping_api/"
    private lateinit var cartViewModel: OshoppingViewModel
    private lateinit var cartRecyclerView: RecyclerView
    var productId:Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        cartRecyclerView = view.findViewById(R.id.cart_recyclerview)
        cartRecyclerView.layoutManager = GridLayoutManager(context, 1)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel.loadCart(1)
        cartViewModel.cartItemLiveData.observe(
            viewLifecycleOwner,
            Observer { carts ->
                Log.d("fetchCart", "Cart fetched successfully ${carts}")
               productId= carts[0].fk_product_id
            })
        cartViewModel.getProductById(9)
        cartViewModel.productItemLiveDataByID.observe(viewLifecycleOwner,Observer { productItem ->
            Log.d("fetchCart", "Cart fetched successfully ${productItem}")
            cart_recyclerview.adapter=CartAdapter(productItem)

        })
    }

    private inner  class CartHolder(itemTextView: View)
        : RecyclerView.ViewHolder(itemTextView),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)

        }

        private lateinit var productItemss: ProductItem


        private val productName = itemView.findViewById(R.id.product_nameTv) as TextView
        private val productDate = itemView.findViewById(R.id.product_category) as TextView
        private val productImage = itemView.findViewById(R.id.product_img) as ImageView


        fun bind(productItems: ProductItem) {
            var compositeProductUrl = url + productItems.product_img
            var conditionString = "string" + productItems.product_img
            if (!conditionString.equals("stringnull"))
                Picasso.get().load(compositeProductUrl).into(productImage)
            productItemss = productItems
            productName.text = productItems.product_name
            productDate.text = productItems.product_date

        }


        override fun onClick(v: View?) {
            Toast.makeText(
                requireContext(),
                "The id: ${productItemss.product_id} and title ${productItemss.product_name} is clicked",
                Toast.LENGTH_LONG
            ).show()
            // callbacks?.onProductSelected(productItemss.product_id)
        }
    }

    private inner class CartAdapter(private val productItems: List<ProductItem>)

        : RecyclerView.Adapter<CartHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CartHolder {
            val View = LayoutInflater.from(parent.context).inflate(R.layout.show_product_in_cart_list_item,parent,false)
            return CartHolder(View)
        }
        override fun getItemCount(): Int = productItems.size

        override fun onBindViewHolder(holder: CartHolder, position: Int) {
            val productItem = productItems[position]
            holder.bind(productItem)
        }
    }


    companion object {
        fun newInstance(): Cart_Fragment {
            return Cart_Fragment()
        }
    }
}
