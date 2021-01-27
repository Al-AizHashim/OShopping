package com.yemen.oshopping.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.yemen.oshopping.Chat.activity.ChatActivity
import com.yemen.oshopping.MainActivity
import com.yemen.oshopping.R
import com.yemen.oshopping.model.Cart
import com.yemen.oshopping.model.ProductItem
import com.yemen.oshopping.sharedPreferences.SharedPreference
import com.yemen.oshopping.utils.Tools
import com.yemen.oshopping.utils.ViewAnimation
import com.yemen.oshopping.viewmodel.OshoppingViewModel

class ProductDetailsActivity : AppCompatActivity() {

    private var parent_view: View? = null
    private var bt_toggle_reviews: ImageButton? = null
    private var bt_toggle_warranty: ImageButton? = null
    private var bt_toggle_description: ImageButton? = null
    private var lyt_expand_reviews: View? = null
    private var lyt_expand_warranty: View? = null
    private var lyt_expand_description: View? = null
    private var nested_scroll_view: NestedScrollView? = null
    private lateinit var frameContainer:FrameLayout
    var productId:Int=1
    lateinit var productImage: CarouselView
    lateinit var productName: TextView
    lateinit var productVendor: TextView
    lateinit var rialProductPrice: TextView
    lateinit var dollarProductPrice: TextView
    lateinit var addToCartFab: FloatingActionButton
    lateinit var ratingBarTexView: TextView
    private var imagesUri: Array<String?>? = null
    private lateinit var productDetails:TextView
    lateinit var sharedPreference: SharedPreference
    lateinit var vendorProfile:TextView
    lateinit var vendorChat:TextView
    // lateinit var productQuantity: TextView
    //lateinit var productDiscount: TextView
    //lateinit var productDetails: TextView
    lateinit var productItem: ProductItem
    lateinit var productItemss: ProductItem
    lateinit var ratingBar: RatingBar
    lateinit var ratingBar2: RatingBar
    lateinit var submitRatingBTN: Button
    private lateinit var productPhotoView: PhotoView
    private lateinit var productPricedialog: TextView
    private lateinit var productNamedialog: TextView
    val delim = ":"
    var list: List<String> = ArrayList()


    var url: String = MainActivity.LOCAL_HOST_URI

    lateinit var oshoppingViewModel: OshoppingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = SharedPreference(applicationContext)
        productId= intent.getIntExtra("PRODUCTID",2)
        Log.d("PRODUCTID", "PRODUCTID in details: $productId")
        setContentView(R.layout.activity_product_details)
        oshoppingViewModel = ViewModelProviders.of(this).get(OshoppingViewModel::class.java)
        productImage = findViewById(R.id.product_img)
        productImage.setImageListener(imageListener)
        productName = findViewById(R.id.product_name)
        //productVendor = view.findViewById(R.id.product_vendor)
        rialProductPrice = findViewById(R.id.product_price_r)
        dollarProductPrice = findViewById(R.id.product_price_d)
        ratingBar = findViewById(R.id.rating_Bar_product_details)
        ratingBarTexView = findViewById(R.id.rating_bar_text_view_prodcut_details)
        parent_view = findViewById(R.id.parent_view)
        productDetails=findViewById(R.id.product_details)
        productVendor=findViewById(R.id.product_vendor)
        initToolbar()
        initComponent()
        frameContainer=findViewById(R.id.fragment_container)
        //val userId=sharedPreference.getValueString("userId")
        //val userName=sharedPreference.getValueString("userName")
        vendorProfile=findViewById(R.id.vendor_profile)
        vendorChat=findViewById(R.id.vendor_chat)


    }

    override fun onStart() {
        super.onStart()
        getProductData()

        vendorChat.setOnClickListener {
            val intent = Intent(this,
                ChatActivity::class.java)
            //vendor id and name from api
            val userId=productItemss.firebase_user_id
            val userName=productItemss.firebase_user_name
            intent.putExtra("userId",userId)
            intent.putExtra("userName",userName)
            startActivity(intent)

            vendorProfile.setOnClickListener {
                val intent2=Intent(this,ShowVendorActivity::class.java)
                intent2.putExtra("VENDORID",productItemss.vendor_id)
                startActivity(intent2)
            }
        }
    }
    fun getProductData(){
        oshoppingViewModel.getProductById(productId)
        oshoppingViewModel.productItemLiveDataByID.observe(
            this,
            Observer { productDetails ->
                productDetails?.let {
                    Log.d("FromObserver", "$it")
                    this.productItem = productDetails[0]
                    productItemss=productItem
                    list = productItem.product_img.split(delim)
                    if (list.size==1)
                        productImage.pageCount =1
                    else
                        productImage.pageCount = list.size -1
                    Log.d("Urlx", "$url+${list}")
                    updateUI()
                }
            })
        productImage.setImageClickListener { position ->
            showDialogImageFull(url+list[position],productItem.dollar_price.toString()+" $",productItem.product_name)
        }
    }

    fun updateUI() {
        productName.text = productItem.product_name
        rialProductPrice.text = productItem.yrial_price.toString() + " RY"
        dollarProductPrice.text = productItem.dollar_price.toString() + " $"
        ratingBar.rating = productItem.rating_average
        ratingBarTexView.text = productItem.number_of_ratings.toString() + " votes"
        productDetails.text=productItem.product_details
        productVendor.text=productItem.first_name+" "+productItem.last_name
    }

    private fun initToolbar() {
        val toolbar =
            findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Fashion")

        Tools().setSystemBarColor(this)
    }

    private fun initComponent() {
        // nested scrollview
        nested_scroll_view =
            findViewById<View>(R.id.nested_scroll_view) as NestedScrollView

        // section reviews
        bt_toggle_reviews = findViewById<View>(R.id.bt_toggle_reviews) as ImageButton
        lyt_expand_reviews =
            findViewById(R.id.lyt_expand_reviews) as View
        bt_toggle_reviews!!.setOnClickListener { view -> toggleSection(view, lyt_expand_reviews) }

        // section warranty
        bt_toggle_warranty =
            findViewById<View>(R.id.bt_toggle_warranty) as ImageButton
        lyt_expand_warranty =
            findViewById(R.id.lyt_expand_warranty) as View
        bt_toggle_warranty!!.setOnClickListener { view -> toggleSection(view, lyt_expand_warranty) }

        // section description
        bt_toggle_description =
            findViewById<View>(R.id.bt_toggle_description) as ImageButton
        lyt_expand_description =
            findViewById(R.id.lyt_expand_description) as View
        bt_toggle_description!!.setOnClickListener { view ->
            toggleSection(
                view,
                lyt_expand_description
            )
        }

        // expand first description
        toggleArrow(bt_toggle_description)
        lyt_expand_description!!.visibility = View.VISIBLE
        addToCartFab = findViewById(R.id.add_to_cart_fab)
        addToCartFab.setOnClickListener {
            val cart = Cart(
                fk_user_id = oshoppingViewModel.getStoredUserId(),
                fk_product_id = productItemss.product_id,
                cart_statuse = 0,
                product_name = productItemss.product_name,
                product_details = productItemss.product_details,
                dollar_price = productItemss.dollar_price,
                yrial_price = productItemss.yrial_price,
                product_quantity = 1,
                vendor_id = productItemss.vendor_id,
                cat_id = productItemss.cat_id,
                product_img = productItemss.product_img,
                product_discount = productItemss.product_discount,
                color = productItemss.color
            )
            Log.d("pushtocart", "the contint of cart is :$cart")
            oshoppingViewModel.pushCart(cart)
            Toast.makeText(
                this,
                "Added to cart successfully",
                Toast.LENGTH_LONG
            ).show()

        }
    }

    private fun toggleSection(bt: View, lyt: View?) {
        val show = toggleArrow(bt)
        if (show) {
            if (lyt != null) {
                ViewAnimation().expand(lyt, object : ViewAnimation.AnimListener {
                    override fun onFinish() {
                        nested_scroll_view?.let { Tools().nestedScrollTo(it, lyt) }
                    }
                })
            }
        } else {
            if (lyt != null) {
                ViewAnimation().collapse(lyt)
            }
        }
    }

    fun toggleArrow(view: View?): Boolean {
        return if (view!!.rotation == 0f) {
            view.animate().setDuration(200).rotation(180f)
            true
        } else {
            view.animate().setDuration(200).rotation(0f)
            false
        }
    }

    var imageListener =
        ImageListener { position, imageView ->
            Picasso.get().load(url + list[position]).into(imageView)
        }

    private fun showDialogImageFull(imageUrl:String,price:String,name:String) {
        val view= layoutInflater.inflate(R.layout.dialog_image,null)
        productPhotoView= view?.findViewById(R.id.product_photo_view_dialog)!!
        productNamedialog = view.findViewById(R.id.product_name_dialog)
        productPricedialog = view.findViewById(R.id.product_price_dialog)
        productPricedialog.text = price
        productNamedialog.text = name
        Picasso.get().load(imageUrl).into(productPhotoView)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(view)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.show()
    }
}