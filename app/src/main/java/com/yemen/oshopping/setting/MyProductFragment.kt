package com.yemen.oshopping.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import com.yemen.oshopping.LoginScreen
import com.yemen.oshopping.MainActivity
import com.yemen.oshopping.R
import com.yemen.oshopping.admin.ShowReportFragmentDirections
import com.yemen.oshopping.model.ProductItem
import com.yemen.oshopping.viewmodel.OshoppingViewModel


class MyProductFragment : Fragment() {

    var url: String = MainActivity.LOCAL_HOST_URI
    private lateinit var oshoppingViewModel: OshoppingViewModel
    private lateinit var showProductRecyclerView: RecyclerView
    private lateinit var noDataImageView: ImageView
    private lateinit var noDataTextView: TextView
    lateinit var deleteProductBtn:Button
    lateinit var fab: FloatingActionButton
    lateinit var productsItems:List<ProductItem>
    val delim = ":"
    var list: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        //the id of vendor should be passed from shared preferences
        if (oshoppingViewModel.getStoredUserId() != -1)
            oshoppingViewModel.apply {
                getProductByVendorId(getStoredUserId())
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_product, container, false)
        showProductRecyclerView = view.findViewById(R.id.category_recycler_view)
        showProductRecyclerView.layoutManager = GridLayoutManager(context, 1)
        fab = view.findViewById(R.id.add_product_fab)
        noDataImageView = view.findViewById(R.id.no_data_imageView)
        noDataTextView = view.findViewById(R.id.no_data_textView)
        if (oshoppingViewModel.getStoredUserId()==-1){
            noDataImageView.visibility=View.VISIBLE
            noDataTextView.visibility=View.VISIBLE
        }
        fab.setOnClickListener {
            if (oshoppingViewModel.getStoredUserId() == -1) {
                Toast.makeText(
                    requireContext(),
                    "You should log in to add product",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(requireContext(), LoginScreen::class.java)
                startActivity(intent)
            }
            else
            Navigation.findNavController(view)
                .navigate(R.id.action_myProductFragment_to_addProductFragment)
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oshoppingViewModel.productItemLiveDataByVendorID.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer
            { productItems ->
                Log.d("productItemLiveData", "product Item Live Data")
                showProductRecyclerView.adapter = ShowProductAdapter(productItems)
                this.productsItems=productItems
            })


        //updateui()
    }


    private fun updateui() {
        oshoppingViewModel.productItemLiveDataByVendorID.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer
            { productItems ->
                Log.d("productItemLiveData", "product Item Live Data")
                this.productsItems=productItems
            })

        showProductRecyclerView.adapter = ShowProductAdapter(productsItems)
    }

    private inner class ShowProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)

        }

        private lateinit var p: ProductItem

        val translateAnimation: Animation = AnimationUtils.loadAnimation(requireContext(),R.anim.translate_anim)
        private val productName = itemView.findViewById(R.id.product_nameTv) as TextView
        private val productDate = itemView.findViewById(R.id.product_category) as TextView
        private val productImage = itemView.findViewById(R.id.product_img) as ImageView
        var mainLayout= itemView.findViewById(R.id.main_layout) as ConstraintLayout
        private val productRating = itemView.findViewById(R.id.rating_Bar_Show_product) as RatingBar

        fun bind(productItems: ProductItem) {

            var imageUri: String
            list = productItems.product_img.split(delim)
            if (list.size == 1)
                imageUri = list[0]
            else
                imageUri = list[0]
            imageUri?.let {
                Picasso.get().load(url + it).into(productImage)
            }
            p = productItems
            productName.text = productItems.product_name
            productDate.text = productItems.product_date
            mainLayout.startAnimation(translateAnimation)
            productRating.rating = productItems.rating_average

        }


        override fun onClick(v: View?) {
            if (view != null) {
                val action= MyProductFragmentDirections.actionMyProductFragmentToUpdateProductFragment(
                        productId = p.product_id,
                        productName = p.product_name,
                        productDetails = p.product_details,
                        productCategory = p.cat_id,
                        productColor = p.color,
                        productDiscount = p.product_discount,
                        productImg = p.product_img,
                        productQuantity = p.product_quantity,
                        productVendor = p.vendor_id,
                        dolarPrice = p.dollar_price.toFloat(),
                        yemenRialPrice = p.yrial_price.toFloat()
                    )

                Navigation.findNavController(view!!)
                    .navigate(action)
            }
        }


    }

    private inner class ShowProductAdapter(private val productItems: List<ProductItem>) :
        RecyclerView.Adapter<ShowProductHolder>() {


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ShowProductHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.show_product_list_item, parent, false)
            return ShowProductHolder(view)
        }

        override fun getItemCount(): Int {
            val numberOfProduct = productItems.size
            if (numberOfProduct < 1) {
                noDataTextView.visibility = View.VISIBLE
                noDataImageView.visibility = View.VISIBLE
            }
            return numberOfProduct
        }

        override fun onBindViewHolder(holder: ShowProductHolder, position: Int) {
            val productItems = productItems[position]
            holder.bind(productItems)

        }
    }


}