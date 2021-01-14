package com.yemen.oshopping.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import com.yemen.oshopping.R
import com.yemen.oshopping.model.ProductItem
import com.yemen.oshopping.viewmodel.OshoppingViewModel

private const val ARG_PARAM1 = "product_id"

class ProductDetailsFragment : Fragment() {
    lateinit var productImage: ImageView
    lateinit var productName: TextView
    lateinit var yrialProductPrice: TextView
    lateinit var dollarProductPrice: TextView
    lateinit var productQuantity: TextView
    lateinit var productDiscount: TextView
    lateinit var productDetails: TextView
    lateinit var productItem: ProductItem
    var url: String = "http://192.168.1.4/oshopping_api/"
    private var param1: Int = 1
    lateinit var oshoppingViewModel: OshoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)

        }
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        oshoppingViewModel.getProductById(param1)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_details, container, false)
        productImage = view.findViewById(R.id.product_img_view)
        productName = view.findViewById(R.id.product_name_text_view)
        yrialProductPrice = view.findViewById(R.id.yrial_price_text_view)
        dollarProductPrice = view.findViewById(R.id.dollar_price_text_view)
        productQuantity = view.findViewById(R.id.prodcut_quantity_text_view)
        productDiscount = view.findViewById(R.id.prodcut_discount_text_view)
        productDetails = view.findViewById(R.id.product_details_text_view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oshoppingViewModel.productItemLiveDataByID.observe(
            viewLifecycleOwner,
            Observer { productDetails ->
                productDetails?.let {
                    Log.d("FromObserver", "$it")
                    this.productItem = productDetails
                    updateUI()
                }
            })
    }

    fun updateUI() {
        productName.text = "product name: " + productItem.product_name
        yrialProductPrice.text =
            "product cost: " + productItem.yrial_price.toString() + "Yemeni rial"
        dollarProductPrice.text = "product cost: " + productItem.dollar_price.toString() + "$"
        productQuantity.text = "Quantity: " + productItem.product_quantity.toString()
        productDiscount.text = "discount: " + productItem.product_discount.toString()
        productDetails.text = "product details: " + productItem.product_details
        var compositeNewsUrl = url + productItem.product_img
        var conditionString = "string" + productItem.product_img
        if (!conditionString.equals("stringnull"))
            Picasso.get().load(compositeNewsUrl).into(productImage)

    }

    companion object {
        fun newInstance() =
            ProductDetailsFragment()

        fun newInstance(param1: Int) =
            ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }


}
